/**
 * The MIT License
 * Copyright (c) 2018 Estonian Information System Authority (RIA),
 * Nordic Institute for Interoperability Solutions (NIIS), Population Register Centre (VRK)
 * Copyright (c) 2015-2017 Estonian Information System Authority (RIA), Population Register Centre (VRK)
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.niis.xroad.restapi.service;

import ee.ria.xroad.signer.protocol.dto.KeyInfo;
import ee.ria.xroad.signer.protocol.dto.TokenInfo;

import lombok.extern.slf4j.Slf4j;
import org.niis.xroad.restapi.exceptions.NotFoundException;
import org.niis.xroad.restapi.facade.SignerProxyFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Service that handles keys
 */
@Slf4j
@Service
@Transactional
@PreAuthorize("denyAll")
public class KeyService {

    private final SignerProxyFacade signerProxyFacade;
    private final TokenService tokenService;

    /**
     * TokenService constructor
     * @param signerProxyFacade
     * @param tokenService
     */
    @Autowired
    public KeyService(SignerProxyFacade signerProxyFacade, TokenService tokenService) {
        this.signerProxyFacade = signerProxyFacade;
        this.tokenService = tokenService;
    }

    /**
     * Return one key
     * @param keyId
     * @throws KeyNotFoundException if key was not found
     * @return
     */
    @PreAuthorize("hasAuthority('VIEW_KEYS')")
    public KeyInfo getKey(String keyId) {
        Collection<TokenInfo> tokens = tokenService.getAllTokens();
        Optional<KeyInfo> keyInfo = tokens.stream()
                .map(TokenInfo::getKeyInfo)
                .flatMap(List::stream)
                .filter(key -> keyId.equals(key.getId()))
                .findFirst();
        if (!keyInfo.isPresent()) {
            throw new KeyNotFoundException("key with id " + keyId + " not found");
        }

        return keyInfo.get();
    }

    public static class KeyNotFoundException extends NotFoundException {
        public KeyNotFoundException(String s) {
            super(s);
        }
    }
}
