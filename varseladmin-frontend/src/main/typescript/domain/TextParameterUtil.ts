/** Hvordan funker egentlig det her (i dokkat)
 * 1. finn parametre i teksten
 * 2. finn eventuelle ekstra direktiver i parametrene
 * 3. Bygg skjema og vis til brukeren
 * 4. send ned innhold og ekstra direktiver for elementer som har ekstra direktiver til backend,
 *    så backend kan deale med det
 * 5. flett sammen prossesserte ekstra-direktiver og enkle parameter i ny liste
 * 6. erstatt parametre med innhold
 */

// Om du leser dette vil jeg anbefale deg å ikke prøve å gjøre noe med denne filen,
// men å heller tenke litt på å bruke VarselFletter.java fra navikt/varsel2

// steg 1 og 2
const parseParametersFromText = (text: string): Map<string, string[]> => {
    const matcher = /\{[^}]+\}/;
    const matches = new Map<string, string[]>();
    var i = 0;
    while (i < text.length) {
        const res = matcher.exec(text.substring(i));
        if (!res) break;
        i = res.index;
        const cleanMatch = (res[1].split(" ")[0]);
        if (matches.has(cleanMatch)) {
            const extraDirectives = res[1].indexOf(' ');
            if (extraDirectives !== -1)
                matches.get(cleanMatch).push(res[1].substring(extraDirectives));
        } else {
            const extraDirectives = res[1].indexOf(' ');
            if (extraDirectives !== -1)
                matches.set(cleanMatch, [res[1].substring(extraDirectives)]);
            else
                matches.set(cleanMatch, []);
        }
    }
    return matches;
}

// steg 3
function processWithDirective(directive: string, value: string): string {
    // TODO: implement
    // det er kun datoformat som må prossesseres her
    // i nåværende løsning gjøres det ved at de pushes ned til backend
    return value;
}

// steg 3 og 4
const buildDataForReplacement = (keysAndDirectives: Map<string, string[]>, content: Map<string, string>): { key: string, value: string }[] => {
    const result = [];
    const entries = keysAndDirectives.entries();
    let keyDirectiveIterator = entries.next();
    while (!keyDirectiveIterator.done) {
        const keyDirectives = keyDirectiveIterator.value;
        if (keyDirectives.value) {
            keyDirectives.value.forEach((directive: string) => {
                result.push(
                    {
                        key: `${keyDirectives.key} ${directive}`, value:
                            processWithDirective(directive, content.get(keyDirectives.key))
                    })
            });
        } else {
            result.push({key: keyDirectives.key, value: content.get(keyDirectives.key)});
        }
        keyDirectiveIterator = entries.next();
    }
    return result;
}

// steg 5
const replaceParametersForTextNaively = (text: string, params: { key: string, value: string }[]): string => {
    return params.reduce(
        (accumulator, currentValue) => {
            return accumulator.replace(new RegExp(`\{${currentValue.key}( [^}])?\}`, 'g'), currentValue.value)
        },
        text
    );
}
