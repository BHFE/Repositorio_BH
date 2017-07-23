/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 *
 * @author Migueljr
 */
public class ReportConection {
    
    static {
        try {
           
            Class.forName( "org.postgresql.Driver" );

            // caso a classe não seja encontrada
        } catch ( ClassNotFoundException exc ) {

            
            exc.printStackTrace();

        }
    }
    public static Connection getConnection(
            String url,
            String usuario,
            String senha ) throws SQLException {

        return DriverManager.getConnection( url, usuario, senha );

    }

    public static Connection getRelatorioConnection() throws SQLException {

        return getConnection(
                "jdbc:postgresql://localhost:5432/bh",
                "postgres",
                "0000" );
    }
}
