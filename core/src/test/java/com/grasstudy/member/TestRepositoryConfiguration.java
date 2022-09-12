package com.grasstudy.member;

import com.grasstudy.member.repository.MemberEntityCallback;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({R2DBCConfiguration.class, MemberEntityCallback.class})
public class TestRepositoryConfiguration {
}
