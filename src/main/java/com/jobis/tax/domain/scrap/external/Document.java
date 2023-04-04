package com.jobis.tax.domain.scrap.external;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ToString
@Getter
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
final public class Document {
    private final String status;
    private final String data;
    private final String errors;
}
