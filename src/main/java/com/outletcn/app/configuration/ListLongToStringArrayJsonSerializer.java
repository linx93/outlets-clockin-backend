package com.outletcn.app.configuration;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * List<Long>类型精度丢失问题
 *
 * @author felix
 */
public class ListLongToStringArrayJsonSerializer extends JsonSerializer<List<Long>> {

    @Override
    public void serialize(List<Long> values, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        String[] newValues = ObjectUtil.defaultIfNull(values, Collections.emptyList()).stream()
                .map(String::valueOf)
                .toArray(String[]::new);
        gen.writeArray(newValues, 0, newValues.length);
    }
}
