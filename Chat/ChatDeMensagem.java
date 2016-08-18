package Chat;


import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Serrano
 */
public class ChatDeMensagem  implements Serializable
{
    private String nome;
    private String texto;
    private String msgReservada;
    private Set<String> usuariosOnline = new HashSet<String>();
    private Action action;
    
    public enum Action
    {
        CONECTADO,DESCONECTADO,ENVIE_PARA_UMA_PESSOA,ENVIE_PARA_TODOS,USUARIOS_ONLINE
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getMsgReservada() {
        return msgReservada;
    }

    public void setMsgReservada(String msgReservada) {
        this.msgReservada = msgReservada;
    }

    public Set<String> getUsuariosOnline() {
        return usuariosOnline;
    }

    public void setUsuariosOnline(Set<String> usuariosOnline) {
        this.usuariosOnline = usuariosOnline;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
    
    
    
}
