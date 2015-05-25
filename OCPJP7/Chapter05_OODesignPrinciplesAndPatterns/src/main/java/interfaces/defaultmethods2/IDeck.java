/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces.defaultmethods2;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public interface IDeck {

    List<ICard> getCards();

    IDeck deckFactory();

    int size();

    void addCard(ICard card);

    void addCards(List<ICard> cards);

    void addDeck(IDeck deck);

    void shuffle();

    void sort();

    void sort(Comparator<ICard> c);

    String deckToString();

    Map<Integer, IDeck> deal(int players, int numberOfCards) throws IllegalArgumentException;
}
