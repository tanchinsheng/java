/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces.defaultmethods2;

/*
 * Copyright (c) 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StandardDeck implements IDeck {

    private List<ICard> entireDeck;

    public StandardDeck(List<ICard> existingList) {
        this.entireDeck = existingList;
    }

    public StandardDeck() {
        entireDeck = new ArrayList<>();
        for (ICard.Suit s : ICard.Suit.values()) {
            for (ICard.Rank r : ICard.Rank.values()) {
                System.out.println("s: " + s + ",r:" + r);
                this.entireDeck.add(new PlayingCard(r, s));
            }
        }
    }

    @Override
    public IDeck deckFactory() {
        return new StandardDeck(new ArrayList<>());
    }

    @Override
    public int size() {
        return entireDeck.size();
    }

    @Override
    public List<ICard> getCards() {
        return entireDeck;
    }

    @Override
    public void addCard(ICard card) {
        entireDeck.add(card);
    }

    @Override
    public void addCards(List<ICard> cards) {
        entireDeck.addAll(cards);
    }

    @Override
    public void addDeck(IDeck deck) {
        List<ICard> listToAdd = deck.getCards();
        entireDeck.addAll(listToAdd);
    }

    @Override
    public void sort() {
        Collections.sort(entireDeck);
    }

    @Override
    public void sort(Comparator<ICard> c) {
        Collections.sort(entireDeck, c);
    }

    @Override
    public void shuffle() {
        Collections.shuffle(entireDeck);
    }

    @Override
    public Map<Integer, IDeck> deal(int players, int numberOfCards)
            throws IllegalArgumentException {
        int cardsDealt = players * numberOfCards;
        int sizeOfDeck = entireDeck.size();
        if (cardsDealt > sizeOfDeck) {
            throw new IllegalArgumentException(
                    "Number of players (" + players
                    + ") times number of cards to be dealt (" + numberOfCards
                    + ") is greater than the number of cards in the deck ("
                    + sizeOfDeck + ").");
        }

        Map<Integer, List<ICard>> dealtDeck = entireDeck
                .stream()
                .collect(
                        Collectors.groupingBy(
                                card -> {
                                    int cardIndex = entireDeck.indexOf(card);
                                    if (cardIndex >= cardsDealt) {
                                        return (players + 1);
                                    } else {
                                        return (cardIndex % players) + 1;
                                    }
                                }));

        // Convert Map<Integer, List<Card>> to Map<Integer, Deck>
        Map<Integer, IDeck> mapToReturn = new HashMap<>();

        for (int i = 1; i <= (players + 1); i++) {
            IDeck currentDeck = deckFactory();
            currentDeck.addCards(dealtDeck.get(i));
            mapToReturn.put(i, currentDeck);
        }
        return mapToReturn;
    }

    @Override
    public String deckToString() {
        return this.entireDeck
                .stream()
                .map(ICard::toString)
                .collect(Collectors.joining("\n"));
    }

    public static void main(String... args) {
        StandardDeck myDeck = new StandardDeck();
        System.out.println("Creating deck:");
        myDeck.sort();
        System.out.println("Sorted deck");
        System.out.println("decktoString: " + myDeck.deckToString());
        myDeck.shuffle();
        myDeck.sort(new SortByRankThenSuit());
        System.out.println("Sorted by rank, then by suit");
        System.out.println(myDeck.deckToString());
        myDeck.shuffle();
        myDeck.sort(
                Comparator.comparing(ICard::getRank)
                .thenComparing(Comparator.comparing(ICard::getSuit)));
        System.out.println("Sorted by rank, then by suit "
                + "with static and default methods");
        System.out.println(myDeck.deckToString());
        myDeck.sort(
                Comparator.comparing(ICard::getRank)
                .reversed()
                .thenComparing(Comparator.comparing(ICard::getSuit)));
        System.out.println("Sorted by rank reversed, then by suit "
                + "with static and default methods");
        System.out.println(myDeck.deckToString());
    }
}
