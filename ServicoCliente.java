
import Chat.ChatDeMensagem;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Serrano
 */
public class ServicoCliente {
    
    private Socket socket;
    private ObjectOutputStream output;
    
    public Socket conectar()
    {
        try {
            this.socket=new Socket("localhost",5555);
            this.output = new ObjectOutputStream(socket.getOutputStream());
            
            
        } catch (IOException ex) {
            Logger.getLogger(ServicoCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        return socket;
    }
    
    public void enviar(ChatDeMensagem mensagem)
    {
        try {
            output.writeObject(mensagem);
        } catch (IOException ex) {
            Logger.getLogger(ServicoCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
