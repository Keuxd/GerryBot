package gerrybot.core;

import java.util.Random;

public class Dices { 
	protected int[] dados;
	protected int numeroVezes;
	protected int numeroLados;
	protected String modificador;
	
	Dices(int numeroVezes, int numeroLados, String modificador){
		this.numeroVezes = numeroVezes;
		this.numeroLados = numeroLados;
		this.modificador = modificador;
	}
	
	Dices(int numeroVezes, int numeroLados){
		this.numeroVezes = numeroVezes;
		this.numeroLados = numeroLados;
	}
	
	public void rodarDados() throws Exception {
		Random rand = new Random();
		int[] numeros = new int[this.numeroVezes];
			
		for(int i = 0; i < this.numeroVezes; i++) {
			numeros[i] = rand.nextInt(this.numeroLados) + 1;
		}
			
		this.dados = numeros;
	}
	
	public int[] getDados() throws Exception {
		if(this.dados == null) {
			rodarDados();
		}
		return this.dados;
	}
	
	public String getConcatDados() {
		StringBuilder resultadoDadosSTR = new StringBuilder();
		resultadoDadosSTR.append("[ " + dados[0]);
		
		int size = dados.length;
		
		for(int i = 1; i < size; i++)
			resultadoDadosSTR.append(" | " + dados[i]);
		
		resultadoDadosSTR.append(" ]");
		
		return resultadoDadosSTR.toString();
	}
	
	public int getTotal() {
		int soma = 0;
		for(int dado : dados) {
			soma += dado;
		}
		
		if(this.modificador != null) {
			if(this.modificador.startsWith("+")) {
				soma += Integer.parseInt(this.modificador.split("\\+")[1]);
			}
			else if(this.modificador.startsWith("-")) {
				soma -= Integer.parseInt(this.modificador.split("-")[1]);
			}
		}
		
		return soma;
	}
	
}
