package com.jpereirax.lightorm.codegen;

public class Constants {

    public static final String SINGLE_LINE_BREAK = "\n";
    public static final String LINE_BREAK = SINGLE_LINE_BREAK + SINGLE_LINE_BREAK;
    public static final String TAB        = "    ";

    public static final String[] DEFAULT_IMPORTS = new String[] {
            "import java.sql.Connection;",
            "import java.sql.PreparedStatement;",
            "import java.sql.ResultSet;",
            "import java.sql.SQLException;",
            "import com.jpereirax.lightorm.datasource.DataSource;"
    };
}
