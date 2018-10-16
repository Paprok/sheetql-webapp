package com.codecool.service.fileManagement;

import com.codecool.exception.MalformedQueryException;
import com.codecool.model.Entry;
import com.codecool.model.Table;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.ValueRange;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Primary
@Service
public class GoogleSheetLoader implements ITableLoader {
    private static final String APPLICATION_NAME = "/home/makiela/Downloads/credentials (1).json";
    private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private static final String TOKENS_DIRECTORY_PATH = "tokens";
    private static final String CREDENTIALS_FILE_PATH = "/credentials.json";
    private static final List<String> SCOPES = Collections.singletonList(SheetsScopes.SPREADSHEETS_READONLY);


    private static Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT) throws Exception {
        InputStream in = GoogleSheetLoader.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .build();
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    @Override
    public Table getTable(String tableName) throws MalformedQueryException {
        try {
            List<List<Object>> values = GetSheetContent(tableName);
            if (values == null || values.isEmpty()) {
                throw new MalformedQueryException("Spreadsheet not found");
            }
            return parseDataFromSheetToTable(values);
        } catch (GeneralSecurityException e) {
            throw new MalformedQueryException("General Security exception");
        } catch (Exception e) {
            throw new MalformedQueryException("Exception during loading Spread Sheet");
        }
    }

    private Table parseDataFromSheetToTable(List<List<Object>> values) {
        Entry headers = parseRowToEntry(values.get(0));
        List<Entry> content = new ArrayList<>();
        for (int i = 1; i < values.size(); i++) {
            content.add(parseRowToEntry(values.get(i)));
        }
        return new Table(headers, content);
    }

    private Entry parseRowToEntry(List<Object> row) {
        List<String> entryData = new ArrayList<>();
        row.forEach(element -> entryData.add((String) element));
        return new Entry(entryData.toArray(new String[0]));
    }

    private List<List<Object>> GetSheetContent(String tableName) throws Exception {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        // Sample Range = Class Data
        final String spreadsheetId = "1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms";
        final String range = tableName.replace("_", " ").replace(";", "");
        Sheets service = new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
        ValueRange response = service.spreadsheets().values()
                .get(spreadsheetId, range)
                .execute();
        return response.getValues();
    }
}
