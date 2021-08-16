package login;

import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.table.*;


public class Conexion {
    
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static final String URL = "jdbc:mysql://localhost:3306/dbmanejousuarios";
    public static final String USUARIO = "root";
    public static final String CONTRASENA = "";
    public boolean resultado = false;
    private DefaultTableModel DT;
    
    
    public static Connection getConnection(){
        Connection Conect = null;
        try{
            Class.forName(DRIVER);
            Conect = (Connection)DriverManager.getConnection(URL, USUARIO, CONTRASENA);
        
        }catch(ClassNotFoundException | SQLException e){
            JOptionPane.showMessageDialog(null,"Error al conectar con la BD" + e);
        }
        return Conect;
    }
    
    private DefaultTableModel setDatos(){
        DT = new DefaultTableModel();
        DT.addColumn("ID");
        DT.addColumn("Nombre de Usuario");
        DT.addColumn("Nombre");
        DT.addColumn("Apellido");
        DT.addColumn("Telefono");
        DT.addColumn("Correo Electronico");
        
        return DT;        
    }
    
    public void validarDatosLogin(String usuario, String contrasena){
        frmUsuariosRegistrados frmUR = new frmUsuariosRegistrados();
        frmRegistroUsuarios frmRU = new frmRegistroUsuarios();
        frmLogin frmlogin = new frmLogin();
        Connection Conect = getConnection();
        
        try{
        String sql = "SELECT * FROM usuarios WHERE NombreUsuario = '"+usuario+"' and Contrasena = '"+contrasena+"' ";
        Statement st = Conect.createStatement();
        ResultSet rs = st.executeQuery(sql);
        
            if(rs.next()){
                resultado = true;
                }
            if(resultado == true){
                    frmUR.setVisible(true);
                    frmlogin.dispose();
                }else{
                    JOptionPane.showMessageDialog(null,"Debe ingresar su usuario y contraseña, si no está registrado debe registrarse");
            }

        }catch(SQLException e){
             JOptionPane.showMessageDialog(null,"Error al conectar con la DB" + e);
        }finally{
            CerrarConexion();
        }
    }
    
    
    
    public void CerrarConexion(){
        Connection conect = getConnection();
        try{
            conect.close();
        }catch(SQLException e){
         JOptionPane.showMessageDialog(null,"error al cerrar conexion "+ e);
        }
        
    }
    
    public void RegistrarUsuarios(String nombreUsuario, String nombre, String apellido, String telefono, String correo, String contrasena){
            Connection CN = getConnection();
            String sql = "INSERT INTO usuarios (NombreUsuario, Nombre, Apellido, NumeroTelefono, CorreoElectronico, Contrasena) values(?,?,?,?,?,?)";
        
            try{
            PreparedStatement PS;
            PS = CN.prepareStatement(sql);
            PS.setString(1, nombreUsuario);
            PS.setString(2, nombre);
            PS.setString(3, apellido);
            PS.setString(4, telefono);
            PS.setString(5, correo);
            PS.setString(6, contrasena);
            int respuesta = PS.executeUpdate();
            
            if(respuesta > 0){
                JOptionPane.showMessageDialog(null,"Registro guardado de manera exitosa");
            }

            }catch(SQLException e){
              JOptionPane.showMessageDialog(null,"error al guardar en la Base de Datos "+ e);
            }finally{
                CerrarConexion();
            }
        }
    
        
        
        public DefaultTableModel getDatos(){
        Connection CN = getConnection();
            String SQLSELECT = "SELECT ID, NombreUsuario, Nombre, Apellido, NumeroTelefono, CorreoElectronico, Contrasena from usuarios";
            
            try{
                setDatos();
                Statement sta = CN.createStatement();
                ResultSet rs = sta.executeQuery(SQLSELECT);
                Object[] filas = new Object[6];
                while(rs.next()){
                    filas[0]= rs.getInt(1);
                    filas[1]= rs.getString(2);
                    filas[2]= rs.getString(3);
                    filas[3]= rs.getString(4);
                    filas[4]= rs.getString(5);
                    filas[5]= rs.getString(6);
                    DT.addRow(filas);
                }
           
            }catch(SQLException e){
              JOptionPane.showMessageDialog(null,"error al guardar en la Base de Datos "+ e);
            }finally{
                CerrarConexion();
            }  
            return DT;
        }
    }
    
   
