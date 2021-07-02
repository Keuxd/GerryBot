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
	
	//TODO optimize it using StringBuilder instead of String
	public String getConcatDados() {
		String resultadoDadosSTR = new String();
		resultadoDadosSTR += "[ " + this.dados[0];
		
		//for comeca em 1 pois o elemento 0 foi colocado diretamente por motivos de formatacao do texto.
		for(int i = 1; i < this.dados.length; i++) {
			resultadoDadosSTR += " | " + this.dados[i];
		}
		
		resultadoDadosSTR += " ]";
		return resultadoDadosSTR;
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
