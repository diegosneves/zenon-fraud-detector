package br.com.zenon.enums;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Currency;
import java.util.Locale;
import java.util.ResourceBundle;

public enum LocaleType {

    PT_BR(Locale.of("pt", "BR"), ZoneId.of("America/Sao_Paulo")),
    EN_US(Locale.of("en", "US"), ZoneId.of("America/New_York"));

    private static final String RESOURCE_BUNDLE_NAME = "messages";
    private final Locale locale;
    private final ZoneId zoneId;

    LocaleType(final Locale locale, final ZoneId zoneId) {
        this.locale = locale;
        this.zoneId = zoneId;
    }

    public String convertToCurrency(final BigDecimal amount) {
        final NumberFormat numberFormat = createCurrencyFormatter();
        return numberFormat.format(amount);
    }

    private NumberFormat createCurrencyFormatter() {
        return NumberFormat.getCurrencyInstance(this.locale);
    }

    public String convertToCurrency(final BigDecimal amount, final String currencyCode) {
        final NumberFormat numberFormat = createCurrencyFormatter();
        numberFormat.setCurrency(Currency.getInstance(currencyCode));
        return numberFormat.format(amount);
    }

    public String formatNumber(final Long value) {
        NumberFormat numberFormat = createIntegerFormatter();
        return numberFormat.format(value);
    }

    private NumberFormat createIntegerFormatter() {
        return NumberFormat.getIntegerInstance(this.locale);
    }

    public String formatNumber(final Integer value) {
        NumberFormat numberFormat = createIntegerFormatter();
        return numberFormat.format(value);
    }

    public String getDateTimeFormatter(final Instant instant) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.LONG);
        return formatter.withLocale(this.locale).withZone(this.zoneId).format(instant);
    }

    public String fetchTranslation(final String key) {
        final ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE_BUNDLE_NAME, this.locale);
        return bundle.getString(key);
    }

}
