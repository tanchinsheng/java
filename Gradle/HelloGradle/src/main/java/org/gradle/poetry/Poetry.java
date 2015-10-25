package org.gradle.poetry;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.codec.binary.Base64;

public class Poetry {

    public List<String> juliusCaesar() {
        List<String> lines = new ArrayList<String>();
        lines.add("O pardon me thou bleeding piece of earth,");
        lines.add("That I am meek and gentle with these butchers!");
        lines.add("Thou art the ruins of the noblest man");
        lines.add("That ever lived in the tide of times.");
        lines.add("Woe to the hand that shed this costly blood!");
        lines.add("Over thy wounds now do I prophesy,");
        lines.add("Which, like dumb mouths, do ope their ruby lips,");
        lines.add("To beg the voice and utterance of my tongue");
        lines.add("A curse shall light upon the limbs of men;");
        lines.add("Domestic fury and fierce civil strife");
        lines.add("Shall cumber all the parts of Italy;");
        lines.add("Blood and destruction shall be so in use");
        lines.add("And dreadful objects so familiar");
        lines.add("That mothers shall but smile when they behold");
        lines.add("Their infants quarter'd with the hands of war;");
        lines.add("All pity choked with custom of fell deeds:");
        lines.add("And Caesar's spirit, ranging for revenge,");
        lines.add("With Ate by his side come hot from hell,");
        lines.add("Shall in these confines with a monarch's voice");
        lines.add("Cry 'Havoc,' and let slip the dogs of war;");
        lines.add("That this foul deed shall smell above the earth");
        lines.add("With carrion men, groaning for burial.");
        return lines;
    }

    public void emit(List<String> lines) {
        for (String line : lines) {
            System.out.println(encode(line));
        }
    }

    public String encode(String text) {
        Base64 codec = new Base64();
        return new String(codec.encode(text.getBytes()));
    }

    public static void main(String[] args) {
        Poetry p = new Poetry();
        p.emit(p.juliusCaesar());
    }
}
