import * as React from 'react';
import {BodyLong, Heading, Modal, Panel} from "@navikt/ds-react";

interface VarselPreviewProps {
    open: boolean,
    onClose: () => void
}

const VarselPreview: React.FC<VarselPreviewProps> = ({open, onClose}) => (<Modal open={open} onClose={onClose}>
    <Modal.Content>
        <Heading size={'large'}>Forhåndsvisning</Heading>
        <Panel border>
            <Heading size={'small'}>SMS</Heading>
            <BodyLong>
                Mauris vel sodales metus, sit amet molestie lectus. Praesent viverra dolor enim, sed rhoncus diam
                dapibus non. Nulla sodales porttitor risus, at tincidunt nunc dictum id. Integer venenatis orci ac diam
                pulvinar blandit. Maecenas id eros felis. Sed tristique luctus ipsum vitae aliquet. Donec consequat ut
                metus interdum posuere.
            </BodyLong>
            <span>Antall tegn: 1337</span>
        </Panel>
        <Panel border>
            <Heading size={'small'}>Epost</Heading>
            <span>Emne: Agurk</span>
            <BodyLong>
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Suspendisse finibus sem ut erat maximus, et
                varius elit elementum. Sed vel orci at felis condimentum ornare in nec lectus. Curabitur ac dictum
                nulla. Vivamus eget tortor lorem. Aliquam luctus mattis est, eu convallis turpis vestibulum sed. Donec
                magna eros, consequat non efficitur eget, faucibus et quam. Maecenas varius tincidunt est eu congue. Sed
                a lectus neque. Donec venenatis eu erat vitae maximus. Donec molestie, justo ac tempus ultricies, risus
                erat facilisis risus, sed lacinia velit justo molestie ante. Duis et volutpat turpis, quis sodales
                nulla. Suspendisse volutpat erat nec massa posuere, ut lacinia augue porta. Sed molestie mi sed semper
                luctus. Aliquam eu condimentum orci. Fusce quis mauris in nibh posuere tempus sit amet id felis.
                Maecenas suscipit aliquet iaculis.
            </BodyLong>
        </Panel>
        <Panel border>
            <Heading size={'small'}>DittNav</Heading>
            <BodyLong>
                Mauris pharetra sagittis urna, et commodo risus efficitur sed. Pellentesque porta facilisis orci a
                tempus. Ut blandit ornare diam at congue. Nam mi enim, porta sed ullamcorper nec, pretium nec justo.
                Nunc ut lorem vel tellus tempor efficitur a in purus. Nam eget metus vel nunc maximus venenatis a nec
                ipsum. Nullam ultricies mauris tortor, ac malesuada risus hendrerit nec. Phasellus augue nunc, euismod
                sit amet mauris eget, auctor volutpat ipsum. Pellentesque habitant morbi tristique senectus et netus et
                malesuada fames ac turpis egestas. Aenean ut sollicitudin nisi. In euismod pretium augue a placerat. Ut
                lorem risus, facilisis a nulla quis, vehicula eleifend diam. Pellentesque sed sapien placerat, auctor
                sem sit amet, interdum neque. Pellentesque orci nisi, bibendum ut rhoncus sit amet, auctor eget sapien.
                Donec cursus ante non purus volutpat placerat. Duis laoreet aliquam eros, ac elementum sem pretium
                feugiat.
            </BodyLong>
            <span>URL: adfga</span>
        </Panel>
    </Modal.Content>
</Modal>);

export default VarselPreview;