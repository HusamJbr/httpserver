package com.husam.CGI;

import com.husam.config.Configuration;
import com.husam.config.ConfigurationManager;
import com.husam.http.HttpRequest;

import java.io.*;

/**
 * Common gateway interface (simple one that just servers html static pages
 */
public class CGI {

    private HttpRequest request;
    private Configuration configuration = ConfigurationManager.getInstance().getCurrentConfiguration();

    public CGI(HttpRequest request) {
        this.request = request;
    }

    public byte[] getResource() {
        File file = new File(configuration.getWebRoot() + "/" + request.getURI());
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString().getBytes();
        } catch (FileNotFoundException e) {
            return pageNotFound().getBytes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private String pageNotFound() {
        return "<html><head></head><body><h1>404 Page Not Found</h2></body></html>";
    }
}
