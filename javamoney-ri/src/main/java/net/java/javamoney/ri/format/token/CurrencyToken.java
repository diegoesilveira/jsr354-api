/*
 *  Copyright (c) 2012, 2013, Credit Suisse (Anatole Tresch), Werner Keil.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * Contributors:
 *    Anatole Tresch - initial implementation
 */
package net.java.javamoney.ri.format.token;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.money.CurrencyUnit;
import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.format.CurrencyFormatter;

import net.java.javamoney.ri.format.common.AbstractFormatToken;
import net.java.javamoney.ri.format.common.FormatToken;

/**
 * {@link FormatToken} that adds a localizable {@link String}, read by key from
 * a {@link ResourceBundle}..
 * 
 * @author Anatole Tresch
 * 
 * @param <T>
 *            The concrete type.
 */
public class CurrencyToken<T extends MonetaryAmount> extends
		AbstractFormatToken<T> {

	public static enum DisplayType {
		NAMESPACE, FULLCODE, CODE, NAME, NUMERIC_CODE, SYMBOL
	}

	private DisplayType displayType = DisplayType.CODE;
	private Locale locale;

	public CurrencyToken() {
	}

	public CurrencyToken(Locale locale) {
		setLocale(locale);
	}

	public CurrencyToken setLocale(Locale locale2) {
		this.locale = locale;
		return this;
	}

	public Locale getLocale() {
		return this.locale;
	}

	public CurrencyToken<T> setDisplayType(DisplayType displayType) {
		if (displayType == null) {
			throw new IllegalArgumentException("Display type null.");
		}
		this.displayType = displayType;
		return this;
	}

	protected String getToken(T item,
			javax.money.format.common.LocalizationStyle style) {
		CurrencyUnit unit = item.getCurrency();
		Locale localeUsed = this.locale;
		if (localeUsed == null) {
			localeUsed = style.getTranslationLocale();
		}
		switch (displayType) {
		case CODE:
			return unit.getCurrencyCode();

		case NAMESPACE:
			return unit.getNamespace();
		case NUMERIC_CODE:
			return String.valueOf(unit.getNumericCode());
		case NAME:
			CurrencyFormatter cf1 = Monetary.getCurrencyFormatterFactory()
					.getCurrencyFormatter(localeUsed);
			return cf1.formatName(unit);
		case SYMBOL:
			CurrencyFormatter cf2 = Monetary.getCurrencyFormatterFactory()
					.getCurrencyFormatter(localeUsed);
			return cf2.formatSymbol(unit);
		case FULLCODE:
		default:
			return unit.getNamespace() + ':' + unit.getCurrencyCode();
		}
	};
}
