/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package helper;

import java.util.Base64;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author albert
 */
public class JwtUtils {
     public static JSONObject decodePayload(String jwtToken) {
        try {
            String[] parts = jwtToken.split("\\.");
            if (parts.length != 3) {
                throw new IllegalArgumentException("Token JWT inválido");
            }

            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            return new JSONObject(payloadJson);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
}
}