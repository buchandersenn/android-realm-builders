package com.github.buchandersenn.realmbuilders.objects;

class MethodNameFormatter {

    String format(String fieldName) {
        if (fieldName == null || fieldName.equals("")) {
            return "";
        }

        // Normalize word separator chars
        fieldName = fieldName.replace('-', '_');

        // Iterate field name using the following rules
        // lowerCase m followed by upperCase anything is considered hungarian notation
        // lowercase char followed by uppercase char is considered camel case
        // Two uppercase chars following each other is considered non-standard camelcase
        // _ and - are treated as word separators
        StringBuilder result = new StringBuilder(fieldName.length());

        if (fieldName.codePointCount(0, fieldName.length()) == 1) {
            result.append(fieldName);
        } else {
            Integer previousCodepoint;
            Integer currentCodepoint = null;
            final int length = fieldName.length();
            for (int offset = 0; offset < length; ) {
                previousCodepoint = currentCodepoint;
                currentCodepoint = fieldName.codePointAt(offset);

                if (previousCodepoint != null) {
                    if (Character.isUpperCase(currentCodepoint) && !Character.isUpperCase(previousCodepoint) && previousCodepoint == 'm' && result.length() == 1) {
                        // Hungarian notation starting with: mX
                        result.delete(0, 1);
                        result.appendCodePoint(Character.toLowerCase(currentCodepoint));

                    } else if (Character.isUpperCase(currentCodepoint) && Character.isUpperCase(previousCodepoint)) {
                        // InvalidCamelCase: XXYx (should have been xxYx)
                        if (offset + Character.charCount(currentCodepoint) < fieldName.length()) {
                            int nextCodePoint = fieldName.codePointAt(offset + Character.charCount(currentCodepoint));
                            if (Character.isLowerCase(nextCodePoint)) {
                                result.append("_");
                            }
                        }
                        result.appendCodePoint(currentCodepoint);

                    } else if (currentCodepoint == '-' || currentCodepoint == '_') {
                        // Word-separator: x-x or x_x
                        result.append("_");

                    } else {
                        // Unknown type
                        result.appendCodePoint(currentCodepoint);
                    }
                } else {
                    // Only triggered for first code point
                    result.appendCodePoint(currentCodepoint);
                }
                offset += Character.charCount(currentCodepoint);
            }
        }

        return result.toString();
    }
}
