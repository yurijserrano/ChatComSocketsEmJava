
import Chat.ChatDeMensagem;
import Chat.ChatDeMensagem.Action;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
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
public class ServicoServidor 
{
    private ServerSocket serverSocket;
    private Socket socket;
    private Map<String,ObjectOutputStream> mapOnlines = new HashMap<String,ObjectOutputStream>();
    
   
    public ServicoServidor()
    {
        try {
            serverSocket = new ServerSocket(5555);
       
            System.out.println("Servidor Online");
            
            while(true)
            {
                socket = serverSocket.accept();
                new Thread(new ListenerSocket(socket)).start();
            }
        
        } catch (IOException ex) {
            Logger.getLogger(ServicoServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

   
    private class ListenerSocket implements Runnable
    {
        private ObjectOutputStream output;
        private ObjectInputStream input;
        
        public ListenerSocket(Socket socket)
        {
            try {
                this.output = new ObjectOutputStream(socket.getOutputStream());
                this.input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ServicoServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        }

        @Override
        public void run() {
        
            ChatDeMensagem mensagem = null;
            
            try {
                
                
                while((mensagem = (ChatDeMensagem) input.readObject())!= null)
                {
                    Action action =mensagem.getAction();
                    
                    if(action.equals(Action.CONECTADO))
                    {
                        
                        boolean conectar=conectar(mensagem,output);
                        if(conectar)
                        {
                            mapOnlines.put(mensagem.getNome(), output);
                            enviaOnline();
                        }
                    }
                    else if(action.equals(Action.DESCONECTADO))
                    {
                        desconectar(mensagem,output);
                        enviaOnline();
                        return;
                    }
                    else if(action.equals(Action.ENVIE_PARA_UMA_PESSOA))
                    {
                        enviarUM(mensagem);
                    }
                    else if(action.equals(Action.ENVIE_PARA_TODOS))
                    {
                        enviarTodos(mensagem,output);
                    }
                    
                    
                }
            } catch (IOException ex) {
                
                desconectar(mensagem,output);
                enviaOnline();
                System.out.println(mensagem.getNome()+" deixou o chat");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServicoServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }
    
    private boolean conectar(ChatDeMensagem mensagem,ObjectOutputStream output)
    {
        if(mapOnlines.size()==0)
        {
            mensagem.setTexto("SIM");
            enviar(mensagem,output);
            return true;
        }
    for(Map.Entry <String, ObjectOutputStream> i: mapOnlines.entrySet())
    {
        if(i.getKey().equals(mensagem.getNome()))
        {
            
            mensagem.setTexto("NÃO");
            enviar(mensagem,output);
            return false;
        }
        else
        {
            mensagem.setTexto("SIM");
            enviar(mensagem,output);
            return true;
        }
    }
    
        return false;
    }
    
    private void desconectar(ChatDeMensagem mensagem,ObjectOutputStream output)
    {
        mapOnlines.remove(mensagem.getNome());
        
        mensagem.setTexto("Até a Próxima");
        
        mensagem.setAction(Action.ENVIE_PARA_UMA_PESSOA);
        
        enviarTodos(mensagem,output);
        
        System.out.println("Usuário "+mensagem.getNome()+" saiu da sala");
        
        
    }
    
    private void enviar(ChatDeMensagem mensagem,ObjectOutputStream output)
    {
        try {
            output.writeObject(mensagem);
        } catch (IOException ex) {
            Logger.getLogger(ServicoServidor.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    private void enviarUM(ChatDeMensagem mensagem)
    {
        for(Map.Entry<String,ObjectOutputStream> i :mapOnlines.entrySet())
        {
            if(i.getKey().equals(mensagem.getMsgReservada()))
            {
                try {
                    i.getValue().writeObject(mensagem);
                } catch (IOException ex) {
                    Logger.getLogger(ServicoServidor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
                
        }
        
    }
    
     private void enviarTodos(ChatDeMensagem mensagem, ObjectOutputStream output) {
       for(Map.Entry<String,ObjectOutputStream> i :mapOnlines.entrySet())
       {
           if(!i.getKey().equals(mensagem.getNome()))
           {
               
               mensagem.setAction(Action.ENVIE_PARA_UMA_PESSOA);
               try {
                   
                   i.getValue().writeObject(mensagem);
               } catch (IOException ex) {
                   Logger.getLogger(ServicoServidor.class.getName()).log(Level.SEVERE, null, ex);
               }
           }
       }
         
         
         
    }
     
    private void enviaOnline()
    {
        Set<String> setNomes = new HashSet<String>();
        
        
        for(Map.Entry<String,ObjectOutputStream> j :mapOnlines.entrySet())
       {
           setNomes.add(j.getKey());
       }
        
           ChatDeMensagem  mensagem= new ChatDeMensagem();
           mensagem.setAction(Action.USUARIOS_ONLINE);
           mensagem.setUsuariosOnline(setNomes);
           for(Map.Entry<String,ObjectOutputStream> i :mapOnlines.entrySet())
           {
               
               mensagem.setNome(i.getKey());
               try {
                   
                   i.getValue().writeObject(mensagem);
               } catch (IOException ex) {
                   Logger.getLogger(ServicoServidor.class.getName()).log(Level.SEVERE, null, ex);
               }
           
       }
    }
    
}
