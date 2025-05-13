-- 사용자 데이터
INSERT INTO users (id, username, password, name, role, created_at, updated_at)
VALUES
    (1, 'owner1@example.com', 'encoded-password', '장묘업체 대표', 'ADMIN', NOW(), NOW()),
    (2, 'owner2@example.com', 'encoded-password', '장묘업체 대표2', 'ADMIN', NOW(), NOW()),
    (3, 'owner3@example.com', 'encoded-password', '장묘업체 대표3', 'ADMIN', NOW(), NOW());

-- 장묘업체 데이터
INSERT INTO funeral_business (
    id, name, address, latitude, longitude, main_image_url,
    business_hours, phone_number, email, description,
    user_id, recommend_rank, created_at, updated_at
)
VALUES
    (1, '포포우즈 장례센터', '서울특별시 강남구 테헤란로 123', 37.495, 127.032,
     'https://example.com/image.jpg', '24시간 연중무휴', '010-1234-5678',
     'funeral@example.com', '반려동물 장례 전문 업체입니다.', 1, 1, NOW(), NOW()),

    (2, '위드펫 메모리얼', '서울특별시 송파구 올림픽로 88', 37.515, 127.105,
     'https://example.com/image2.jpg', '10:00 ~ 22:00', '010-9876-5432',
     'withpet@example.com', '정성스러운 장례 서비스를 제공합니다.', 2, 2, NOW(), NOW()),

    (3, '하늘로 떠난 친구들', '경기도 성남시 분당구 정자동 456', 37.383, 127.122,
     'https://example.com/image3.jpg', '09:00 ~ 20:00', '010-5555-7777',
     'heaven@example.com', '작별 인사를 도와드리는 공간입니다.', 3, 3, NOW(), NOW());