import {useNavigate, useParams} from "react-router-dom";
import {useState} from "react";

export default function ItemUpdatePage() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [formData, setFormData] = useState(null);

    const handleChange = (e) => {
        const { name, value } = e.target;
        setFormData((prev) => ({ ...prev, [name]: value }));
    };

    const handleCancel = () => {
        navigate(`/items/detail/${id}`);
    };

    const handleSubmit = () => {
        console.log("수정 요청:", formData);
    };

    const actions = [
        { label: "저장", type: "submit", onClick: handleSubmit },
        { label: "취소", type: "cancel", onClick: handleCancel },
    ];

    if (!formData) return <div className="p-4">불러오는 중...</div>;

    return (
        <PageLayout
            title="📦 품목 수정"
            breadcrumb={["품목 관리", "품목 수정"]}
            actions={<ButtonLayout actions={actions} />}
        >
            <ItemForm formData={formData} onChange={handleChange} />
        </PageLayout>
    );
}