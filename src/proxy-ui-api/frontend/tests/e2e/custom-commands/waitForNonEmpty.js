/*
 * The MIT License
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
const Events = require('events');

module.exports = class WaitForNonEmpty extends Events {
    // async command(f) {
    async command(selector) {
        console.log('waitForValue, waiting for non-empty string value for selector ' + selector);
        const interval = 100;
        const timeout = 5000;
        let counter = 0;
        while ((counter * interval) < timeout) {
            let nonEmptyString = false;
            await this.api.getValue(selector, (result) => {
                if (typeof(result.value) == 'string' && result.value.length > 0) {
                    // if (result.hasOwnProperty('value') && typeof(result.value) == 'string' && result.value.length > 0) {
                    nonEmptyString = true;
                }
            })
            if (nonEmptyString) {
                console.log("wait complete");
                let returnValue = {
                    status: 1,
                };
                this.emit('complete', returnValue);
                return returnValue;
            }
            await new Promise(r => setTimeout(r, interval));
            counter++;
        }
        // var err = new Error().stack
        // this command does not know the stacktrace (which test the command was called from)
        // maybe changing it to be non-async (it that makes sense) would help?
        this.emit("error", new Error(`Timeout exceeded while waiting for non-empty string value for selector [${selector}]`));
    }
}