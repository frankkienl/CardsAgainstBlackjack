package nl.frankkie.cab.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by FrankkieNL on 31-1-2015.
 */
public class CardsContract {
    
    public static final String CONTENT_AUTHORITY = "nl.frankkie.cab";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);    
    public static final String PATH_BLACK_CARD = "black_card";
    public static final String PATH_WHITE_CARD = "white_card";
    public static final String PATH_CARD_SET = "card_set";
    public static final String PATH_CARD_SET_BLACK_CARD = "card_set_black_card";    
    public static final String PATH_CARD_SET_WHITE_CARD = "card_set_white_card";   
    
    public static final class CardSetEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CARD_SET).build();
        public static final String CONTENT_ITEM_TYPE = 
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CARD_SET;
        public static final String CONTENT_TYPE = 
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CARD_SET;        
        public static final String TABLE_NAME = PATH_CARD_SET;

        public static final String COLUMN_NAME_ACTIVTE = "active";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_BASEDESK = "base_deck";
        public static final String COLUMN_NAME_DESCRIPTION = "description";
        public static final String COLUMN_NAME_WEIGHT = "weight";
    }
    
    public static final class BlackCardEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_BLACK_CARD).build();
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_BLACK_CARD;
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_BLACK_CARD;
        public static final String TABLE_NAME = PATH_BLACK_CARD;
        
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_DRAW = "draw";
        public static final String COLUMN_NAME_PICK = "pick";
        public static final String COLUMN_NAME_WATERMARK = "watermark";
    }
    
    public static final class WhiteCardEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_WHITE_CARD).build();
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_WHITE_CARD;
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_WHITE_CARD;
        public static final String TABLE_NAME = PATH_WHITE_CARD;
        
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_WATERMARK = "watermark";
    }
        
    public static final class CardSetBlackCard implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CARD_SET_BLACK_CARD).build();
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CARD_SET_BLACK_CARD;
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CARD_SET_BLACK_CARD;
        public static final String TABLE_NAME = PATH_CARD_SET_BLACK_CARD;
        
        public static final String COLUMN_NAME_CARD_SET_ID = "card_set_id";
        public static final String COLUMN_NAME_BLACK_CARD_ID = "black_card_id";
    }

    public static final class CardSetWhiteCard implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_CARD_SET_WHITE_CARD).build();
        public static final String CONTENT_ITEM_TYPE =
                "vnd.android.cursor.item/" + CONTENT_AUTHORITY + "/" + PATH_CARD_SET_WHITE_CARD;
        public static final String CONTENT_TYPE =
                "vnd.android.cursor.dir/" + CONTENT_AUTHORITY + "/" + PATH_CARD_SET_WHITE_CARD;
        public static final String TABLE_NAME = PATH_CARD_SET_WHITE_CARD;

        public static final String COLUMN_NAME_CARD_SET_ID = "card_set_id";
        public static final String COLUMN_NAME_WHITE_CARD_ID = "white_card_id";
    }
}
