export const itemListActions = (onDownload) => [
    {label: "등록", type: "create", to: "/items/new"},
    {label: "엑셀 다운로드", type: "download", onClick: onDownload}
];

export const itemDetailActions = (id, onDelete) => [
    { label: "수정", type: "update", to: '/items/update'},
    { label: "삭제", type: "delete", onClick: onDelete},
    { label: "목록", type: "list", to: "/items/list" }
];