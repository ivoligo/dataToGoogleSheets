import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.AppendValuesResponse;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.SpreadsheetProperties;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

public class App {
    
    // Build a new authorized API client service.
    private static final String SPREADSHEET_ID = "1ivkdeaY0cR0beQCejOQKooOBwcrfE6vM2Z8WhNxV2xs"; //https://docs.google
    // .com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit#gid=0
    private static final String RANGE = "myList!A1:E";
    
    private static Sheets getSheetsService() throws IOException, GeneralSecurityException {
        return new Sheets.Builder(GoogleNetHttpTransport.newTrustedTransport(), SheetsQuickstart.JSON_FACTORY,
                                  SheetsQuickstart.getCredentials(GoogleNetHttpTransport.newTrustedTransport())).setApplicationName(SheetsQuickstart.APPLICATION_NAME)
                .build();
    }
    
    public static void main(String[] args) throws IOException, GeneralSecurityException {
        
        
        ValueRange response = getSheetsService().spreadsheets().values().get(SPREADSHEET_ID, RANGE).execute();
        List<List<Object>> values = response.getValues();
        if (values == null || values.isEmpty()) {
            System.out.println("No data found.");
        } else {
            System.out.println("one, two");
            for (List row : values) {
                // Print columns A and E, which correspond to indices 0 and 4.
                System.out.printf("%s, %s\n", row.get(0), row.get(4));
            }
        }
        
        setDataInSheets();
        createSpreadsheet();
    }
    
    private static void setDataInSheets() throws IOException, GeneralSecurityException {
        
        ValueRange appendBody = new ValueRange().setValues(Arrays.asList(Arrays.asList("updated_A", "updated_B", "updated_C", "updated_D", "updated_E")));
        AppendValuesResponse appendResult = getSheetsService().spreadsheets()
                .values()
                .append(SPREADSHEET_ID, "myList", appendBody)
                .setValueInputOption("USER_ENTERED")
                .setInsertDataOption("INSERT_ROWS")
                .setIncludeValuesInResponse(true)
                .execute();
        //        ValueRange body = new ValueRange().setValues(Arrays.asList(Arrays.asList("updated")));
        //        UpdateValuesResponse result = getSheetsService().spreadsheets().values().update(SPREADSHEET_ID, "C5", body).setValueInputOption("RAW").execute();
        System.out.println("проверьте. ок");
    }
    
    private static void createSpreadsheet() throws IOException, GeneralSecurityException {
        Spreadsheet spreadsheet = new Spreadsheet().setProperties(new SpreadsheetProperties().setTitle("myTest0"));
        spreadsheet = getSheetsService().spreadsheets().create(spreadsheet).setFields("spreadsheetId").execute();
        System.out.println("Spreadsheet ID: " + spreadsheet.getSpreadsheetId());
    }
}
