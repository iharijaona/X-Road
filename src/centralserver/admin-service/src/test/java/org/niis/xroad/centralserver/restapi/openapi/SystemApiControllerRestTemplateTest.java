/**
 * The MIT License
 *
 * Copyright (c) 2019- Nordic Institute for Interoperability Solutions (NIIS)
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
package org.niis.xroad.centralserver.restapi.openapi;

import org.junit.Before;
import org.junit.Test;
import org.niis.xroad.centralserver.openapi.model.HighAvailabilityStatus;
import org.niis.xroad.centralserver.openapi.model.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.niis.xroad.centralserver.restapi.util.TestUtils.addApiKeyAuthorizationHeader;

public class SystemApiControllerRestTemplateTest extends AbstractApiControllerTestContext {

    @Autowired
    TestRestTemplate restTemplate;

    @Before
    public void setUp() {
        addApiKeyAuthorizationHeader(restTemplate);
    }

    @Test
    @WithMockUser(authorities = {"VIEW_VERSION"})
    public void testViewVersionRestEndpoint() {
        ResponseEntity<Version> response = restTemplate.getForEntity("/api/v1/system/version", Version.class);
        assertNotNull(response, "System Version response  must not be null.");
        assertEquals(200, response.getStatusCodeValue(), "Version response status code must be 200 ");
        assertNotNull(response.getBody());
        assertEquals(ee.ria.xroad.common.Version.XROAD_VERSION, response.getBody().getInfo());
    }

    @Test
    @WithMockUser(authorities = {"HIGH_AVAILABILITY_STATUS"})
    public void testGetHighAvailabilityStatusRestEndpoint() {
        ResponseEntity<HighAvailabilityStatus> response =
                restTemplate.getForEntity("/api/v1/system/high-availability-status", HighAvailabilityStatus.class);
        assertNotNull(response, "High availability status response must not be null.");
        assertEquals(200, response.getStatusCodeValue(), "High availability response status code must be 200 ");
        assertNotNull(response.getBody());
        assertEquals(false, response.getBody().getIsHaConfigured());
        assertEquals("node_0", response.getBody().getNodeName());
    }
}