/*-
 * #%L
 * de.hs-heilbronn.mi:crawler4j-core
 * %%
 * Copyright (C) 2010 - 2022 crawler4j-fork (pre-fork: Yasser Ganjisaffar)
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */
package edu.uci.ics.crawler4j.parser;

import java.net.URI;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSExpressionMemberTermURI;
import com.helger.css.decl.CSSImportRule;
import com.helger.css.decl.ICSSTopLevelRule;
import com.helger.css.decl.visit.DefaultCSSUrlVisitor;

public class CssUrlExtractVisitor extends DefaultCSSUrlVisitor {
	
	private final URI referenceAbsoluteUrl;
	private final Set<URI> seedUrls = new LinkedHashSet<>();
	
	
	public CssUrlExtractVisitor(final URI referenceAbsoluteUrl) {
		this.referenceAbsoluteUrl = referenceAbsoluteUrl;
	}
	
	
	
	@Override
	public void onImport(@Nonnull final CSSImportRule aImportRule) {
		addSeedUrl(URI.create(aImportRule.getLocationString()));
	}
	
	
	@Override
	public void onUrlDeclaration(@Nullable final ICSSTopLevelRule aTopLevelRule,
			@Nonnull final CSSDeclaration aDeclaration, @Nonnull final CSSExpressionMemberTermURI aURITerm)
	{
		addSeedUrl(URI.create(aURITerm.getURIString()));
	}
	
	
	
	/**
	 * @return the added seed url or empty
	 */
	protected Optional<URI> addSeedUrl(final URI url) {
		if (url == null || url.toString().startsWith("data:")) {
			return Optional.empty();
		}
		final URI seedUrl = referenceAbsoluteUrl.resolve(url);
		seedUrls.add(seedUrl);
		return Optional.of(seedUrl);
	}


	public URI getReferenceAbsoluteUrl() {
		return referenceAbsoluteUrl;
	}
	
	public Set<URI> getSeedUrls() {
		return Collections.unmodifiableSet(seedUrls);
	}
}
