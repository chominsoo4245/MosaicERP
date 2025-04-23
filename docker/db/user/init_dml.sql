-- 유저
INSERT INTO public.tb_user (id, login_id, password, email, status_code, created_at, update_at, last_login_at, role_id) VALUES (1, 'admin', '$2a$10$0N7hXrJdezk9A.ydg5fNR.6NoEoc0DQ4eLBUo.zGhXrLJcg6naDwy', null, 'ACTIVE', '2025-04-03 07:03:11.198540', '2025-04-03 07:03:11.198540', null, 1);

-- 유저 상세

-- 역할
INSERT INTO public.tb_auth_role (id, name, description) VALUES (1, 'ROLE_ADMIN', '최고 관리자');
INSERT INTO public.tb_auth_role (id, name, description) VALUES (2, 'ROLE_MANAGER', '중간 관리자');
INSERT INTO public.tb_auth_role (id, name, description) VALUES (3, 'ROLE_USER', '일반 사용자');

-- 유저 상태
INSERT INTO public.tb_user_status (status_code, description) VALUES ('ACTIVE', '정상 사용자');
INSERT INTO public.tb_user_status (status_code, description) VALUES ('INACTIVE', '휴먼 사용자');
INSERT INTO public.tb_user_status (status_code, description) VALUES ('SUSPENDED', '정지 사용자');
INSERT INTO public.tb_user_status (status_code, description) VALUES ('WITHDRAWAL_REQUESTED', '탈퇴 요청된 사용자 (복구 가능 기간)');
INSERT INTO public.tb_user_status (status_code, description) VALUES ('PENDING_DELETE', '삭제 대기 사용자 (개인정보 만료 대상)');
INSERT INTO public.tb_user_status (status_code, description) VALUES ('WITHDRAWN', '회원 탈퇴 완료 (개인정보 최소 보관 기간 대기중)');
