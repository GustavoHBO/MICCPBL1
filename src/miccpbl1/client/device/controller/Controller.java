/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package miccpbl1.client.device.controller;

import miccpbl1.client.device.view.exceptions.InvalidHeartBeatsException;
import miccpbl1.client.device.view.exceptions.NullHeartBeatsException;
import miccpbl1.client.device.view.exceptions.NullStatusMovementException;

/**
 *
 * @author gustavo
 */
public class Controller {
    
    private static Controller controller = null;
    
    public static Controller getController(){
        if(controller == null){
            controller = new Controller();
        }
        return controller;
    }
    
    public static void resetController (){
        controller = null;
    }
    
    private void receiverData(String data){
        
    }
    
    private void sendData(String data){
        
    }
    
    public void cadastrarPaciente(String nome, String cpf, String numero, int btmCardiacos, boolean statusMovimento, String pressaoSanguinea, boolean acEspecial) {
        
        String data = "00";
        data += nome + "!-" + cpf + "!-" + numero + "!-" + btmCardiacos + "!-" + statusMovimento + "!-" + pressaoSanguinea + "!-" + acEspecial + "00";
        sendData(data);
    }
    
    public void updateStatusPatient(String btmCardiacos, String statusMovimento, String pressaoSanguinea, String patientRisk) throws NullHeartBeatsException, InvalidHeartBeatsException, NullStatusMovementException{
        
        String data = "01";
        if(btmCardiacos==null){
            throw new NullHeartBeatsException();
        }
        else if(btmCardiacos.trim().isEmpty()){
            throw new InvalidHeartBeatsException();
        }        
        
        if(statusMovimento == null){
            throw new NullStatusMovementException();
        }
        data += btmCardiacos + "!-" + statusMovimento + "!-" + pressaoSanguinea + "!-" + patientRisk + "01";
        sendData(data);
    }
    
    public void updateRangeRefresh(String time){
        String data = "02";
        data += time + "02";
        sendData(data);
    }
}
