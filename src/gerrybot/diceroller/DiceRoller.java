package gerrybot.diceroller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;

import javax.swing.DefaultListModel;

import net.dv8tion.jda.api.entities.User;

public class DiceRoller {
	
	private String[] rolledMessages;
	private String[] modifiers;
	private int[][] rolledDices;
	
	private int dicesSum;
	private int modifersSum;
	
	private User author;
	
	public DiceRoller(String rollMessage, User author) {
		this.author = author;
		processMessage(rollMessage);
	}
	
	private void processMessage(String rollMessage) {
		String[] splittedMessage = rollMessage.split("(?=[+-])");
		Predicate<String> containsD = s -> s.contains("d");
		rolledMessages = Arrays.stream(splittedMessage).filter(containsD).toArray(String[]::new);
		modifiers = Arrays.stream(splittedMessage).filter(containsD.negate()).toArray(String[]::new);
				
		processDice(rolledMessages);
		
		dicesSum = sumDices();
		modifersSum = sumModifiers();
	}
	
	private void processDice(String[] rolledMessages) {
		rolledDices = new int[rolledMessages.length][];
		for(int i = 0; i < rolledMessages.length; i++) {
			String[] currentRoll = rolledMessages[i].split("d");
			currentRoll[0] = currentRoll[0].replaceFirst("[+-]", "");			
			rolledDices[i] = roll(currentRoll[0].isEmpty() ? 1 : Integer.parseInt(currentRoll[0]), Integer.parseInt(currentRoll[1]));
		}
	}
	
	public int[] roll(int amount, int sides) {
		int[] rolledDices = new int[amount];
		ArrayList<String> drRolls = DRFrame.getInstance().getDRRollsFromUser(author);
		DefaultListModel<String> model = DRFrame.getInstance().getDRListModel();
		
		for(int i = 0; i < amount; i++) {
			boolean hasDr = false;
			
			for(int j = 0; j < drRolls.size(); j++) {
				String[] drs = drRolls.get(j).split("=");
				if(Integer.parseInt(drs[1]) == sides) {
					int value = Integer.parseInt(drs[2]);
					
					if(value <= sides) {
						rolledDices[i] = value;
						model.removeElement(drRolls.get(j));
						drRolls.remove(j);
						hasDr = true;
						break;
					}
				}
			}
			
			if(!hasDr) {
				rolledDices[i] = new Random().nextInt(sides) + 1;				
			}
		}
		
		return rolledDices;
	}
	
	private int sumDices() {
		int sum = 0;
		
		for(int i = 0; i < rolledDices.length; i++) {
			switch(rolledMessages[i].charAt(0)) {
				case '-':
					sum -= Arrays.stream(rolledDices[i]).sum();
					break;
				case '+':
				default:
					sum += Arrays.stream(rolledDices[i]).sum();
			}
		}
		
		return sum;
	}
	
	private int sumModifiers() {
		int sum = 0;
		
		for(int i = 0; i < modifiers.length; i++) {
			int currentValue = Integer.parseInt(modifiers[i].substring(1));
			switch(modifiers[i].charAt(0)) {
				case '-':
					sum -= currentValue;
					break;
				case '+':
				default:
					sum += currentValue;
			}
		}
		
		return sum;
	}
	
	public String dicesToString() {
		StringBuilder sb = new StringBuilder();
		
		for(int i = 0; i < rolledDices.length; i++) {
			sb.append("[ " + rolledDices[i][0]);
			for(int j = 1; j < rolledDices[i].length; j++) {
				sb.append(" | " + rolledDices[i][j]);
			}
			sb.append(" ] \n");
		}
		sb.deleteCharAt(sb.length() - 1);
		
		if(modifiers.length != 0) {
			sb.append(String.format("%+d", modifersSum).replaceAll("([+-])(\\d)", "$1 $2"));
		}
		
		return sb.toString();
	}
	
	public int getTotalSum() {
		return dicesSum + modifersSum;
	}
	
	public void DEBUG_VALUES() {
		System.out.println("\nRolledMessages -> " + Arrays.toString(rolledMessages));
		System.out.println("ModifierMessages -> " + Arrays.toString(modifiers));
		System.out.println("Final Rolls -> " + Arrays.deepToString(rolledDices));
		System.out.println("Dices Sum -> " + dicesSum);
		System.out.println("Modifiers Sum -> " + modifersSum + "\n");
	}
}