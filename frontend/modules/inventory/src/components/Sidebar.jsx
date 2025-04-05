import { Link } from "react-router-dom";
import SidebarMenuTree from "./SidebarMenuTree";

const menus = [
    {
        label: "품목 관리",
        path: null,
        children: [
            {
                label: "품목 목록",
                path: "/items/list"
            },
            {
                label: "품목 등록",
                path: "/items/register"
            },
        ]
    },
    {
        label: "입고 관리",
        path: null,
        children: [
            {
                label: "입고 등록",
                path: "/inbound/register",
            },
            {
                label: "입고 이력",
                path: "/inbound/list"
            }
        ]
    },
    {
        label: "출고 관리",
        path: null,
        children: [
            {
                label: "출고 등록",
                path: "/outbound/register"
            },
            {
                label: "출고 이력",
                path: "/outbound/list"
            }
        ]
    },
    {
        label: "재고 현황",
        path: null,
        children: [
            {
                label: "현재 재고",
                path: "/stock-status/list"
            },
            {
                label: "수량",
                path: "/stock-status/quantity"
            },
            {
                label: "위치 정보",
                path: "/stock-status/locations"
            }
        ]
    },
    {
        label: "재고 이력",
        path: null,
        children: [
            {
                label: "전체 히스토리",
                path: "/stock-history/list",
            }
        ]
    },
    {
        label: "설정",
        path: null,
        children: [
            {
                label: "재고 단위",
                path: "/setting/units",
            },
            {
                label: "분류 코드",
                path: "/setting/codes",
            }
        ]
    }
];

export default function Sidebar() {
    return (
        <aside className="w-64 bg-zinc-100 shadow-md py-4 px-6 flex flex-col justify-between">
            <div className="mb-8">
                <h1 className="text-xl font-bold text-blue-600">
                    <Link to="/">📦 재고 관리 서비스</Link>
                </h1>
            </div>

            <nav className="flex-1 overflow-auto">
                <ul>
                    <SidebarMenuTree items={menus} />
                </ul>
            </nav>

            <div className="mt-8 border-t pt-4">
                <p className="text-sm text-gray-600 mb-1">👤 홍길동 대리</p>
                <button className="text-red-500 hover:underline text-sm">로그아웃</button>
            </div>
        </aside>
    );
}
