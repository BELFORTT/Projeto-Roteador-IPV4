package br.edu.ifpb.poo.roteador;

import java.util.ArrayList;
import lombok.Getter;

@Getter
public class Roteador {
    private final ArrayList<Rota> rotas;
    private final ArrayList<InterfaceFisica> interfaces;

    public Roteador() {
        this.rotas = new ArrayList<>();
        this.interfaces = new ArrayList<>();
    }

    //Método UC01
    public boolean cadastrarInterface(String nome, String ip) {

        for (InterfaceFisica interfaceF : this.interfaces) {
            if (interfaceF.getNome().equalsIgnoreCase(nome)) {
                return false;
            } 
        }

        // Cadastra a Interface se não existir uma com o nome passado
        InterfaceFisica novaInterface = new InterfaceFisica(nome, ip);
        this.interfaces.add(novaInterface);
        return true;
    }

    // Método UC02
    public boolean cadastrarRota(String destino, String gateway, String mascara, InterfaceFisica interfac) {
        
        for (Rota rotas : this.rotas) {
            if (rotas.getEnderecoDestino().equals(destino) && rotas.getMascaraDeSubRede().equals(mascara) && rotas.getInterfac().equals(interfac)) {
                return false;
            }
        }

        Rota novaRota = new Rota(destino, gateway, mascara, interfac);
        this.rotas.add(novaRota);
        return true;
    }

    // Método UC03

    // Método UC04

    // Método UC05

    // Método UC07
}
