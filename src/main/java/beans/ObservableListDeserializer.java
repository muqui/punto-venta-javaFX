/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package beans;


import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.IOException;

public class ObservableListDeserializer extends StdDeserializer<ObservableList<OrderDetail>> {

    public ObservableListDeserializer() {
        this(null);
    }

    protected ObservableListDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public ObservableList<OrderDetail> deserialize(JsonParser jp, DeserializationContext ctxt)
            throws IOException, JsonProcessingException {

        ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode root = mapper.readTree(jp);

        ObservableList<OrderDetail> orderDetails = FXCollections.observableArrayList();
        if (root.isArray()) {
            for (JsonNode node : root) {
                OrderDetail detail = mapper.readValue(node.toString(), OrderDetail.class);
                orderDetails.add(detail);
            }
        }

        return orderDetails;
    }
}
