import React from "react";

export default function ItemForm({ formData, onChange }) {
    return (
        <div className="grid grid-cols-2 gap-4 p-4 bg-white border rounded shadow">
            <div>
                <label className="block font-semibold mb-1">품목명</label>
                <input
                    name="name"
                    value={formData.name}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    placeholder="예: 고급 나사"
                />
            </div>
            <div>
                <label className="block font-semibold mb-1">품목 코드</label>
                <input
                    name="code"
                    value={formData.code}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    placeholder="예: A-1001"
                />
            </div>
            <div>
                <label className="block font-semibold mb-1">초기 재고 수량</label>
                <input
                    type="number"
                    name="stock"
                    value={formData.stock}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                />
            </div>
            <div>
                <label className="block font-semibold mb-1">단위</label>
                <input
                    name="unit"
                    value={formData.unit}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    placeholder="예: EA"
                />
            </div>
            <div>
                <label className="block font-semibold mb-1">단가</label>
                <input
                    type="number"
                    name="price"
                    value={formData.price}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    placeholder="예: 1000"
                />
            </div>
            <div>
                <label className="block font-semibold mb-1">분류 코드</label>
                <input
                    name="category"
                    value={formData.category}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    placeholder="예: 나사류"
                />
            </div>
            <div>
                <label className="block font-semibold mb-1">공급업체</label>
                <input
                    name="supplier"
                    value={formData.supplier}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    placeholder="예: A상사"
                />
            </div>
            <div>
                <label className="block font-semibold mb-1">위치 정보</label>
                <input
                    name="location"
                    value={formData.location}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    placeholder="예: 창고 A-1"
                />
            </div>
            <div>
                <label className="block font-semibold mb-1">LOT 번호</label>
                <input
                    name="lot"
                    value={formData.lot}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    placeholder="예: LOT-20240401"
                />
            </div>
            <div>
                <label className="block font-semibold mb-1">유통기한</label>
                <input
                    type="date"
                    name="expirationDate"
                    value={formData.expirationDate}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                />
            </div>
            <div className="col-span-2">
                <label className="block font-semibold mb-1">설명</label>
                <textarea
                    name="description"
                    value={formData.description}
                    onChange={onChange}
                    className="w-full p-2 border rounded"
                    rows="4"
                    placeholder="이 품목에 대한 설명을 입력하세요."
                />
            </div>
        </div>
    );
}
