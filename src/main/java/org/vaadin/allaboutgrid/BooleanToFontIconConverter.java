/*
 * Copyright 2000-2014 Vaadin Ltd.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.vaadin.allaboutgrid;

import java.util.Locale;

import com.vaadin.data.util.converter.Converter;
import com.vaadin.server.FontAwesome;

public class BooleanToFontIconConverter implements Converter<String, Boolean> {

    @Override
    public Boolean convertToModel(String value,
            Class<? extends Boolean> targetType, Locale locale)
            throws ConversionException {
        throw new ConversionException("Not supported");
    }

    @Override
    public String convertToPresentation(Boolean value,
            Class<? extends String> targetType, Locale locale)
            throws com.vaadin.data.util.converter.Converter.ConversionException {
        if (Boolean.TRUE.equals(value)) {
            return FontAwesome.CHECK_SQUARE_O.getHtml();
        } else {
            return FontAwesome.SQUARE_O.getHtml();
        }
    }

    @Override
    public Class<Boolean> getModelType() {
        return Boolean.class;
    }

    @Override
    public Class<String> getPresentationType() {
        return String.class;
    }

}
