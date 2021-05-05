package ca.on.conestogac.kmarshalldlacelle.matchgame;

import android.widget.ImageButton;


public class Card {

    private ImageButton card;
    private int imageId;
    private boolean matched;

    public Card(ImageButton _facade, int _id) {
        card = _facade;
        imageId = _id;
        matched = false;
    }

    public ImageButton getCard() {
        return card;
    }

    public void setCard(ImageButton card) {
        this.card = card;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public boolean isMatched() {
        return matched;
    }

    public void setMatched(boolean matched) {
        this.matched = matched;
    }
}
