-- 카테고리
INSERT INTO public.tb_category (category_id, parent_category_id, category_type, category_code, name, description, level, created_at, updated_at, short_code) VALUES (1, null, 'ITEM', '10000', '전체 아이템', '아이템 서비스의 최상위 카테고리', 1, '2025-04-14 14:49:42.389645', '2025-04-14 14:49:42.389645', 'A');
INSERT INTO public.tb_category (category_id, parent_category_id, category_type, category_code, name, description, level, created_at, updated_at, short_code) VALUES (2, 1, 'ITEM', '11000', '완제품', '완제품 분류', 2, '2025-04-14 14:49:42.389645', '2025-04-14 14:49:42.389645', 'A001');
INSERT INTO public.tb_category (category_id, parent_category_id, category_type, category_code, name, description, level, created_at, updated_at, short_code) VALUES (3, 1, 'ITEM', '12000', '원자재', '원자재 분류', 2, '2025-04-14 14:49:42.389645', '2025-04-14 14:49:42.389645', 'A002');

-- 아이템
INSERT INTO public.tb_item (item_id, category_id, code, name, description, unit, cost, price, is_lot_tracked, default_supplier_id, created_at, updated_at) VALUES (1, 1, 'ELEC-001', '스마트폰', '최신 스마트폰 모델', 'EA', 300.00, 500.00, true, 101, '2025-04-11 12:58:16.550671', '2025-04-11 12:58:16.550671');
INSERT INTO public.tb_item (item_id, category_id, code, name, description, unit, cost, price, is_lot_tracked, default_supplier_id, created_at, updated_at) VALUES (2, 1, 'ELEC-002', '노트북', '고성능 노트북', 'EA', 700.00, 1200.00, true, 102, '2025-04-11 12:58:16.550671', '2025-04-11 12:58:16.550671');
INSERT INTO public.tb_item (item_id, category_id, code, name, description, unit, cost, price, is_lot_tracked, default_supplier_id, created_at, updated_at) VALUES (3, 2, 'HOME-001', '냉장고', '에너지 효율 높은 냉장고', 'EA', 400.00, 800.00, false, 103, '2025-04-11 12:58:16.550671', '2025-04-11 12:58:16.550671');
INSERT INTO public.tb_item (item_id, category_id, code, name, description, unit, cost, price, is_lot_tracked, default_supplier_id, created_at, updated_at) VALUES (4, 3, 'OFF-001', '프린터', '칼라 프린터', 'EA', 150.00, 300.00, false, 104, '2025-04-11 12:58:16.550671', '2025-04-11 12:58:16.550671');
INSERT INTO public.tb_item (item_id, category_id, code, name, description, unit, cost, price, is_lot_tracked, default_supplier_id, created_at, updated_at) VALUES (5, 2, 'ZZZ-0001', '미친 나사', '1243', '4123', null, 1243.00, false, null, '2025-04-14 15:25:39.523097', '2025-04-14 15:25:39.523097');

-- 공급업체
INSERT INTO public.tb_supplier (supplier_id, name, contact_details, address, created_at, updated_at) VALUES (1, 'ABC Electronics', '010-1234-5678', '서울시 강남구 역삼동 123', '2025-04-11 12:58:16.575090', '2025-04-11 12:58:16.575090');
INSERT INTO public.tb_supplier (supplier_id, name, contact_details, address, created_at, updated_at) VALUES (2, 'XYZ Components', '02-9876-5432', '부산시 해운대구 센텀동 456', '2025-04-11 12:58:16.575090', '2025-04-11 12:58:16.575090');
INSERT INTO public.tb_supplier (supplier_id, name, contact_details, address, created_at, updated_at) VALUES (3, 'Global Supplies', '031-555-0101', '대구시 중구 동성로 789', '2025-04-11 12:58:16.575090', '2025-04-11 12:58:16.575090');

--
INSERT INTO public.tb_warehouse (warehouse_id, name, address_line1, address_line2, city, state, zipcode, country, capacity, created_at, updated_at) VALUES (1, '서울 본사 창고', '서울특별시 중구 명동 10', '지하 1층', '서울', '서울특별시', '04532', '대한민국', 1000, '2025-04-16 13:28:04.637062', '2025-04-16 13:28:04.637062');
INSERT INTO public.tb_warehouse (warehouse_id, name, address_line1, address_line2, city, state, zipcode, country, capacity, created_at, updated_at) VALUES (2, '부산 지점 창고', '부산광역시 해운대구 센텀동 20', null, '부산', '부산광역시', '48088', '대한민국', 800, '2025-04-16 13:28:04.637062', '2025-04-16 13:28:04.637062');

