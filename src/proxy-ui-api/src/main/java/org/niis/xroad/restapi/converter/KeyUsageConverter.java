/**
 * The MIT License
 * Copyright (c) 2018 Estonian Information System Authority (RIA),
 * Nordic Institute for Interoperability Solutions (NIIS), Population Register Centre (VRK)
 * Copyright (c) 2015-2017 Estonian Information System Authority (RIA), Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.niis.xroad.restapi.converter;

import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Convert X598Certificate's public abstract boolean[] getKeyUsage()
 */
@Component
@SuppressWarnings("checkstyle:MagicNumber") // index numbers are most clear way here to represent the issue
public class KeyUsageConverter {
    private static final Map<Integer, org.niis.xroad.restapi.openapi.model.Certificate.KeyUsagesEnum> BIT_TO_USAGE =
            new ConcurrentHashMap<>();
    static {
        BIT_TO_USAGE.put(0, org.niis.xroad.restapi.openapi.model.Certificate.KeyUsagesEnum.DIGITAL_SIGNATURE);
        BIT_TO_USAGE.put(1, org.niis.xroad.restapi.openapi.model.Certificate.KeyUsagesEnum.NON_REPUDIATION);
        BIT_TO_USAGE.put(2, org.niis.xroad.restapi.openapi.model.Certificate.KeyUsagesEnum.KEY_ENCIPHERMENT);
        BIT_TO_USAGE.put(3, org.niis.xroad.restapi.openapi.model.Certificate.KeyUsagesEnum.DATA_ENCIPHERMENT);
        BIT_TO_USAGE.put(4, org.niis.xroad.restapi.openapi.model.Certificate.KeyUsagesEnum.KEY_AGREEMENT);
        BIT_TO_USAGE.put(5, org.niis.xroad.restapi.openapi.model.Certificate.KeyUsagesEnum.KEY_CERT_SIGN);
        BIT_TO_USAGE.put(6, org.niis.xroad.restapi.openapi.model.Certificate.KeyUsagesEnum.CRL_SIGN);
        BIT_TO_USAGE.put(7, org.niis.xroad.restapi.openapi.model.Certificate.KeyUsagesEnum.ENCIPHER_ONLY);
        BIT_TO_USAGE.put(8, org.niis.xroad.restapi.openapi.model.Certificate.KeyUsagesEnum.DECIPHER_ONLY);

    }

    /**
     * Convert boolean array of key usage bits as returned by
     * https://docs.oracle.com/javase/8/docs/api/java/security/cert/X509Certificate.html#getKeyUsage--
     * into a Set of enums
     *
     *
     *
     * @param keyUsageBits
     * @return
     */
    public Set<org.niis.xroad.restapi.openapi.model.Certificate.KeyUsagesEnum> convert(boolean[] keyUsageBits) {
        Set<org.niis.xroad.restapi.openapi.model.Certificate.KeyUsagesEnum> usages = new HashSet<>();
        if (keyUsageBits != null) {
            for (int i = 0; i < Math.min(BIT_TO_USAGE.size(), keyUsageBits.length); i++) {
                if (keyUsageBits[i]) {
                    usages.add(BIT_TO_USAGE.get(i));
                }
            }
        }
        return usages;
    }
}
