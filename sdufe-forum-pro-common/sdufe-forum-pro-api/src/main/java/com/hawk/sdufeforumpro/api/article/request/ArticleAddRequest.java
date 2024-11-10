package com.hawk.sdufeforumpro.api.article.request;

import com.hawk.sdufeforumpro.base.request.BaseRequest;
import lombok.*;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ArticleAddRequest extends BaseRequest {

    private String content;

    private String userId;
}
