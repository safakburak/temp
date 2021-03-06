package com.ekinoksyazilim.etkk.prototype.configuration;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractConfiguration {

    private String serialize(Object val) {

        if (val instanceof String) {

            return (String) val;

        } else if (val instanceof Integer || val.getClass() == int.class) {

            return val.toString();

        } else if (val instanceof Float || val.getClass() == float.class) {

            return val.toString();

        } else if (val instanceof Boolean || val.getClass() == boolean.class) {

            return (Boolean) val == true ? "true" : "false";

        } else if (val instanceof Color) {

            Color col = (Color) val;
            return String.format("%02X%02X%02X", col.getRed(), col.getGreen(), col.getBlue()).toUpperCase();

        } else if (val instanceof InetSocketAddress) {

            InetSocketAddress add = (InetSocketAddress) val;
            return add.getHostName() + ":" + add.getPort();

        } else {

            throw new RuntimeException("unsupported config type! : " + val.getClass().getSimpleName());
        }
    }

    private Object deserialize(String text, Class<?> clazz) {

        if (clazz == String.class) {

            return text;

        } else if (clazz == Integer.class || clazz == int.class) {

            return Integer.parseInt(text);

        } else if (clazz == Float.class || clazz == float.class) {

            return Float.parseFloat(text);

        } else if (clazz == Boolean.class || clazz == boolean.class) {

            return Boolean.parseBoolean(text);

        } else if (clazz == Color.class) {

            return new Color(Integer.parseInt(text.substring(0, 2), 16), Integer.parseInt(text.substring(2, 4), 16),
                    Integer.parseInt(text.substring(4, 6), 16));

        } else if (clazz == InetSocketAddress.class) {

            String[] tokens = text.split(":");
            return new InetSocketAddress(tokens[0], Integer.parseInt(tokens[1]));

        } else if (Map.class.isAssignableFrom(clazz)) {

            return null;

        } else {

            throw new RuntimeException("unsupported config type!");
        }
    }

    protected abstract String getPath();

    public void save() {

        List<Field> fields = new ArrayList<>();

        for (Field field : getClass().getDeclaredFields()) {

            if (field.isAnnotationPresent(config.class)) {

                fields.add(field);
            }
        }

        Collections.sort(fields, new Comparator<Field>() {

            @Override
            public int compare(Field o1, Field o2) {

                return o1.getAnnotation(config.class).value().compareTo(o2.getAnnotation(config.class).value());
            }
        });

        StringBuilder builder = new StringBuilder();

        builder.append("<properties>\n");

        try {

            for (Field field : fields) {

                if (field.isAnnotationPresent(config.class)) {

                    field.setAccessible(true);

                    config ann = field.getAnnotation(config.class);

                    Object value = field.get(this);

                    if (value != null) {

                        if (value instanceof Map) {

                            @SuppressWarnings("unchecked")
                            Map<String, Object> map = (Map<String, Object>) value;

                            for (String key : map.keySet()) {

                                builder.append("<entry key=\"" + ann.value() + "." + key + "\">"
                                        + serialize(map.get(key)) + "</entry>\n");
                            }

                        } else {

                            builder.append("<entry key=\"" + ann.value() + "\">" + serialize(value) + "</entry>\n");
                        }
                    }
                }
            }

            builder.append("</properties>\n");

            FileWriter writer = new FileWriter(getPath());
            writer.write(builder.toString());
            writer.flush();
            writer.close();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public void load() {

        Map<String, Field> fieldMap = new HashMap<>();

        for (Field field : getClass().getDeclaredFields()) {

            if (field.isAnnotationPresent(config.class)) {

                config ann = field.getAnnotation(config.class);

                fieldMap.put(ann.value(), field);
            }
        }

        try {

            BufferedReader reader = new BufferedReader(new FileReader(getPath()));

            String line;

            Pattern keyPattern = Pattern.compile("key=\"(.*)\"");
            Pattern valPattern = Pattern.compile("<entry key=\".*\">(.*)</entry>");

            int lineNo = 0;

            while ((line = reader.readLine()) != null) {

                lineNo++;

                if (line.startsWith("<entry ")) {

                    Matcher keyMatcher = keyPattern.matcher(line);
                    Matcher valMatcher = valPattern.matcher(line);

                    if (keyMatcher.find() && valMatcher.find()) {

                        String key = keyMatcher.group(1);
                        String val = valMatcher.group(1);

                        Field field = null;

                        if (fieldMap.containsKey(key)) {

                            field = fieldMap.get(key);

                            field.setAccessible(true);

                            field.set(this, deserialize(val, field.getType()));

                        } else {

                            String parentKey = new String(key);

                            while (field == null && parentKey.contains(".")) {

                                parentKey = parentKey.substring(0, parentKey.lastIndexOf("."));

                                if (fieldMap.containsKey(parentKey)) {

                                    field = fieldMap.get(parentKey);

                                    if (Map.class.isAssignableFrom(field.getType())) {

                                        field.setAccessible(true);

                                        @SuppressWarnings("rawtypes")
                                        Map map = (Map) field.get(this);

                                        if (map == null) {

                                            map = new TreeMap<>();
                                            field.set(this, map);
                                        }

                                        ParameterizedType type = (ParameterizedType) field.getGenericType();

                                        String childKey = key.substring(parentKey.length() + 1);

                                        map.put(childKey,
                                                deserialize(val, (Class<?>) type.getActualTypeArguments()[1]));

                                    } else {

                                        field = null;
                                    }
                                }
                            }
                        }

                        if (field == null) {

                            reader.close();
                            throw new RuntimeException("unknown key: " + key);
                        }

                    } else {

                        reader.close();
                        throw new RuntimeException("config cannot be parsed! line: " + lineNo);
                    }
                }
            }

            reader.close();

        } catch (FileNotFoundException e) {

            save();

        } catch (IOException e) {

            e.printStackTrace();

        } catch (IllegalArgumentException e) {

            e.printStackTrace();

        } catch (IllegalAccessException e) {

            e.printStackTrace();
        }
    }

    public Map<String, Object> getGroup(String prefix) {

        Map<String, Object> result = new HashMap<>();

        for (Field field : getClass().getDeclaredFields()) {

            field.setAccessible(true);

            if (field.isAnnotationPresent(config.class)) {

                config ann = field.getAnnotation(config.class);

                if (ann.value().startsWith(prefix)) {

                    try {

                        result.put(ann.value().substring(prefix.length()), field.get(this));

                    } catch (IllegalArgumentException e) {

                        e.printStackTrace();

                    } catch (IllegalAccessException e) {

                        e.printStackTrace();

                    }
                }
            }
        }

        return result;
    }

    protected static Color color(String text) {

        return new Color(Integer.parseInt(text.substring(0, 2), 16), Integer.parseInt(text.substring(2, 4), 16),
                Integer.parseInt(text.substring(4, 6), 16));
    }
}
