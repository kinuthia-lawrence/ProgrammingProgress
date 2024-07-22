package com.larrykin.Controllers;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Document;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ResourceBundle;

public class HelpController implements Initializable {

    @FXML
    private AnchorPane helpAnchorPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            String markdownContent = new String(Files.readAllBytes(Paths.get("HELP.md")));
            Parser parser = Parser.builder().build();
            Document document = parser.parse(markdownContent);
            HtmlRenderer renderer = HtmlRenderer.builder().build();
            String htmlContent = renderer.render(document);

            WebView webView = new WebView();
            webView.getEngine().loadContent(htmlContent);
            webView.getEngine().setUserStyleSheetLocation(getClass().getResource("/STYLES/darkTheme.css").toString());

            helpAnchorPane.getChildren().add(webView);
            //? Set the AnchorPane constraints to the WebView
            AnchorPane.setTopAnchor(webView, 0.0);
            AnchorPane.setBottomAnchor(webView, 0.0);
            AnchorPane.setLeftAnchor(webView, 0.0);
            AnchorPane.setRightAnchor(webView, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
