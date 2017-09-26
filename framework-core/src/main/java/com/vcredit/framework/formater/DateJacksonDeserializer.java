package com.vcredit.framework.formater;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.vcredit.framework.util.DateUtil;

public class DateJacksonDeserializer extends JsonDeserializer<Date> {

	@Override
	public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		Date date = null;
		String dateText = p.getText();
		if (dateText != null) {
			try {
				date = DateUtil.strToDate(dateText);
			} catch (ParseException e) {
				try {
					date = DateUtil.getDate(dateText);
				} catch (ParseException e1) {
					throw new JsonMappingException("date property formater exception");
				}
			}

		}
		return date;

	}

}
