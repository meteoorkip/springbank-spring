package nl.springbank.helper;

import nl.springbank.bean.CardBean;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Tristan de Boer
 * @author Sven Konings
 */
public class CardHelper {
    private static final int CARD_NUMBER_SIZE = 4;
    private static final int PIN_NUMBER_SIZE = 4;

    /**
     * Get a random card number that is not in the given list of existing cards.
     *
     * @param existingCards the given list of existig cards
     * @return the random card number string
     */
    public static String getRandomCardNumber(List<CardBean> existingCards) {
        List<String> existingCardNumbers = existingCards.stream()
                .map(CardBean::getCardNumber)
                .collect(Collectors.toList());
        String cardNumber;
        do {
            cardNumber = getRandomNumber(CARD_NUMBER_SIZE);
        } while (existingCardNumbers.contains(cardNumber));
        return cardNumber;
    }

    /**
     * Get a random pin number.
     *
     * @return the random pin number string
     */
    public static String getRandomPin() {
        return getRandomNumber(PIN_NUMBER_SIZE);
    }

    private static String getRandomNumber(int size) {
        StringBuilder builder = new StringBuilder(size);
        Random random = new Random();
        for (int i = 0; i < size; i++) {
            builder.append(random.nextInt(10));
        }
        return builder.toString();
    }

    /**
     * Get the card expiration date, which is 5 years from the current time
     *
     * @return the expiration date
     */
    public static Date getExpirationDate() {
        Calendar calendar = DateHelper.getCalendar();
        calendar.add(Calendar.YEAR, 5);
        return calendar.getTime();
    }
}
