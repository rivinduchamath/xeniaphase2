package com.cloudofgoods.xenia.controller.controllconfig;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseOperator {
    String type;
    Map<Object , Object> operators = new HashMap <> ();
}
