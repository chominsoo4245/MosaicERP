import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import PageLayout from "../../components/Layout/PageLayout.jsx";
import ButtonLayout from "../../components/Layout/ButtonLayout.jsx";
import Tabs from "../../components/Tabs.jsx";
import ItemForm from "./form/ItemForm.jsx";
import { getItemAPI } from "../../services/ItemService.jsx";

export default function ItemDetailPage() {
    const { id } = useParams(); // URL: /items/detail/:id
    const navigate = useNavigate();
    const [item, setItem] = useState(null);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);
    const [activeTab, setActiveTab] = useState("basic");

    const actions = [
        { label: "수정", type: "update", to: `/items/update/${id}` },
        { label: "목록", type: "list", onClick: () => navigate("/items/list") },
    ];

    useEffect(() => {
        const fetchItem = async () => {
            try {
                const response = await getItemAPI(id);
                if (response.success) {
                    setItem(response.data);
                } else {
                    setError(response.message || "품목 상세 정보를 불러오는데 실패했습니다.");
                }
            } catch (err) {
                setError(err.message || "품목 상세 정보를 불러오는데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };
        fetchItem();
    }, [id]);

    if (loading)
        return (
            <PageLayout title="품목 상세 정보" breadcrumb={["품목 관리", "상세 보기"]}>
                <p>Loading...</p>
            </PageLayout>
        );
    if (error)
        return (
            <PageLayout title="품목 상세 정보" breadcrumb={["품목 관리", "상세 보기"]}>
                <p className="text-red-500">{error}</p>
            </PageLayout>
        );
    if (!item) return null;

    return (
        <PageLayout
            title="📦 품목 상세 정보"
            breadcrumb={["품목 관리", "상세 보기"]}
            actions={<ButtonLayout actions={actions} />}
        >
            <Tabs activeTab={activeTab} setActiveTab={setActiveTab} tabs={[
                { id: "basic", label: "기본 정보" },
                { id: "inventory", label: "재고 정보" },
                { id: "lot", label: "LOT 정보" },
                { id: "audit", label: "감사 정보" },
            ]}>
                {activeTab === "basic" && (
                    // 상세 조회의 경우, mode="detail"이면 폼 내 자동 생성 필드는 읽기 전용으로 노출
                    <ItemForm formData={item} onChange={() => {}} readOnly mode="detail" />
                )}
                {activeTab === "inventory" && (
                    <div>
                        <h3 className="font-semibold mb-2">재고 정보</h3>
                        <p><strong>현재 재고:</strong> {item.inventory?.currentQuantity || 0}</p>
                        <p><strong>예약 재고:</strong> {item.inventory?.reservedQuantity || 0}</p>
                        <p>
                            <strong>창고:</strong> {item.inventory?.warehouse?.name} <br />
                            <span>{item.inventory?.warehouse?.address}</span>
                        </p>
                    </div>
                )}
                {activeTab === "lot" && (
                    <div>
                        <h3 className="font-semibold mb-2">LOT 정보</h3>
                        {item.lotDetails && item.lotDetails.length > 0 ? (
                            item.lotDetails.map((lot) => (
                                <div key={lot.lotId} className="border rounded p-2 mb-2">
                                    <p><strong>LOT 번호:</strong> {lot.lotNumber}</p>
                                    <p><strong>초기 재고:</strong> {lot.initialStock}</p>
                                    <p><strong>유통기한:</strong> {lot.expirationDate}</p>
                                    <p><strong>위치:</strong> {lot.locationInfo}</p>
                                    <p><strong>상태:</strong> {lot.status}</p>
                                </div>
                            ))
                        ) : (
                            <p>LOT 정보가 없습니다.</p>
                        )}
                    </div>
                )}
                {activeTab === "audit" && (
                    <div>
                        <h3 className="font-semibold mb-2">생성 및 수정 정보</h3>
                        <p><strong>생성일:</strong> {item.createdAt}</p>
                        <p><strong>최종 수정일:</strong> {item.updatedAt}</p>
                    </div>
                )}
            </Tabs>
        </PageLayout>
    );
}
