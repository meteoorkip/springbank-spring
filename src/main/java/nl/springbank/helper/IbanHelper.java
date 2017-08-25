package nl.springbank.helper;

import nl.springbank.bean.AccountBean;
import org.iban4j.CountryCode;
import org.iban4j.Iban;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Generates iBAN keys.
 *
 * @author Tristan de Boer
 * @author Sven Konings
 */
public class IbanHelper {
    private static final String BANK_CODE = "INGB";

    /**
     * Generate an iban string that is not used by any of the accounts in the given list of existing accounts.
     *
     * @param existingAccounts the given list of existing accounts
     * @return the generated iban
     */
    public static String generateIban(List<AccountBean> existingAccounts) {
        List<String> existingIbans = existingAccounts.stream()
                .map(AccountBean::getIban)
                .collect(Collectors.toList());
        String iban;
        do {
            iban = new Iban.Builder()
                    .countryCode(CountryCode.NL)
                    .bankCode(BANK_CODE)
                    .buildRandom().toString();
        } while (existingIbans.contains(iban));
        return iban;
    }
}
