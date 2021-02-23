package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.util.Base64;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Text Encrypter Decrypter");
        GridPane gp = new GridPane();

        TextArea textArea = new TextArea();
        GridPane.setConstraints(textArea, 0,0);

        Label key = new Label("Key:\n Must be 16 bytes");
        GridPane.setConstraints(key, 1,0);

        TextField Key = new TextField();
        GridPane.setConstraints(Key, 1,1);

        Button Encrypt = new Button("Encrypt");
        GridPane.setConstraints(Encrypt,0,2);
        Encrypt.setOnAction(event -> {
            try {
                String Password = Key.getText();
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

                byte[] keyByte  = Password.getBytes("UTF-8");
                SecretKeySpec secretKey = new SecretKeySpec(keyByte, "AES");
                IvParameterSpec ivparameterspec = new IvParameterSpec(keyByte);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivparameterspec);
                byte[] cipherText = cipher.doFinal(textArea.getText().getBytes("UTF8"));
                Base64.Encoder encoder = Base64.getEncoder();
                String encryptedText = encoder.encodeToString(cipherText);
                textArea.clear();
                textArea.setText(encryptedText);


            }catch(Exception e){
                e.printStackTrace();
            }
        });


        Button Decrypt = new Button("Decrypt");
        GridPane.setConstraints(Decrypt, 1,2);
        Decrypt.setOnAction(event->{
            try {
                String Password = Key.getText();
                Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

                byte[] keyByte = Password.getBytes("UTF-8");
                SecretKeySpec secretKey = new SecretKeySpec(keyByte, "AES");
                IvParameterSpec ivparameterspec = new IvParameterSpec(keyByte);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivparameterspec);
                Base64.Decoder decoder = Base64.getDecoder();
                byte[] cipherText = decoder.decode(textArea.getText().getBytes("UTF8"));
                String decryptedText = new String(cipher.doFinal(cipherText), "UTF-8");

                textArea.clear();
                textArea.setText(decryptedText);
            }catch(Exception e){
                e.printStackTrace();
            }
        });

        gp.getChildren().addAll(Decrypt,Encrypt,Key, key, textArea);
        primaryStage.setScene(new Scene(gp, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
