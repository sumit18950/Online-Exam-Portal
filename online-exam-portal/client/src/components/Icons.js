const s = { width: '1em', height: '1em', fill: 'currentColor' };
const d = (props = {}) => {
  const mergedStyle = { verticalAlign: '-0.125em', ...(props.style || {}) };
  return { ...s, ...props, style: mergedStyle };
};

export const GraduationCap = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M12 3L1 9l4 2.18v6L12 21l7-3.82v-6l2-1.09V17h2V9L12 3zm6.82 6L12 12.72 5.18 9 12 5.28 18.82 9zM17 15.99l-5 2.73-5-2.73v-3.72L12 15l5-2.73v3.72z"/></svg>
);

export const BookOpen = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M21 4H3a1 1 0 0 0-1 1v14a1 1 0 0 0 1 1h18a1 1 0 0 0 1-1V5a1 1 0 0 0-1-1zM4 18V6h7v12H4zm16 0h-7V6h7v12z"/></svg>
);

export const ClipboardCheck = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M19 3h-4.18C14.4 1.84 13.3 1 12 1s-2.4.84-2.82 2H5a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h14a2 2 0 0 0 2-2V5a2 2 0 0 0-2-2zm-7 0a1 1 0 1 1 0 2 1 1 0 0 1 0-2zm-2 14-4-4 1.41-1.41L10 14.17l6.59-6.59L18 9l-8 8z"/></svg>
);

export const Users = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M16 11a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm-8 0a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm0 2c-2.33 0-7 1.17-7 3.5V19h14v-2.5c0-2.33-4.67-3.5-7-3.5zm8 0c-.29 0-.62.02-.97.05 1.16.84 1.97 1.97 1.97 3.45V19h6v-2.5c0-2.33-4.67-3.5-7-3.5z"/></svg>
);

export const FileText = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M14 2H6a2 2 0 0 0-2 2v16a2 2 0 0 0 2 2h12a2 2 0 0 0 2-2V8l-6-6zm4 18H6V4h7v5h5v11zM8 13h8v2H8v-2zm0 4h8v2H8v-2zm0-8h3v2H8V9z"/></svg>
);

export const BarChart = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M5 9.2h3V19H5V9.2zM10.6 5h2.8v14h-2.8V5zm5.6 8H19v6h-2.8v-6z"/></svg>
);

export const Clock = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20zm0 18a8 8 0 1 1 0-16 8 8 0 0 1 0 16zm.5-13H11v6l5.25 3.15.75-1.23-4.5-2.67V7z"/></svg>
);

export const Shield = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M12 1L3 5v6c0 5.55 3.84 10.74 9 12 5.16-1.26 9-6.45 9-12V5l-9-4zm0 10.99h7c-.53 4.12-3.28 7.79-7 8.94V12H5V6.3l7-3.11v8.8z"/></svg>
);

export const Award = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M12 2a7 7 0 0 0-4.65 12.23L6 22l6-3 6 3-1.35-7.77A7 7 0 0 0 12 2zm0 12a5 5 0 1 1 0-10 5 5 0 0 1 0 10z"/></svg>
);

export const Play = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M8 5v14l11-7z"/></svg>
);

export const CheckCircle = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20zm-2 15-5-5 1.41-1.41L10 14.17l7.59-7.59L19 8l-9 9z"/></svg>
);

export const UserPlus = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M15 12a4 4 0 1 0 0-8 4 4 0 0 0 0 8zm0-6a2 2 0 1 1 0 4 2 2 0 0 1 0-4zm0 8c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4zm-6 4c.22-.72 3.31-2 6-2s5.78 1.28 6 2H9zM6 15V12h3v-2H6V7H4v3H1v2h3v3h2z"/></svg>
);

export const Edit = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M3 17.25V21h3.75L17.81 9.94l-3.75-3.75L3 17.25zM20.71 7.04a1 1 0 0 0 0-1.41l-2.34-2.34a1 1 0 0 0-1.41 0l-1.83 1.83 3.75 3.75 1.83-1.83z"/></svg>
);

export const Trash = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M6 19a2 2 0 0 0 2 2h8a2 2 0 0 0 2-2V7H6v12zM19 4h-3.5l-1-1h-5l-1 1H5v2h14V4z"/></svg>
);

export const Plus = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M19 13h-6v6h-2v-6H5v-2h6V5h2v6h6v2z"/></svg>
);

export const Key = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M12.65 10a6 6 0 1 0 0 4H17v4h4v-4h2v-4H12.65zM7 14a2 2 0 1 1 0-4 2 2 0 0 1 0 4z"/></svg>
);

export const User = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M12 12a4 4 0 1 0 0-8 4 4 0 0 0 0 8zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z"/></svg>
);

export const Layers = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M11.99 18.54l-7.37-5.73L3 14.07l9 7 9-7-1.63-1.27-7.38 5.74zM12 16l7.36-5.73L21 9l-9-7-9 7 1.63 1.27L12 16z"/></svg>
);

export const Globe = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M12 2a10 10 0 1 0 0 20 10 10 0 0 0 0-20zm-1 17.93A8 8 0 0 1 4.07 13H7v-2H4.07A8 8 0 0 1 11 4.07V7h2V4.07A8 8 0 0 1 19.93 11H17v2h2.93A8 8 0 0 1 13 19.93V17h-2v2.93z"/></svg>
);

export const Zap = (props) => (
  <svg {...d(props)} viewBox="0 0 24 24"><path d="M11 21h-1l1-7H7.5a.5.5 0 0 1-.4-.8l5-8.5H13l-1 7h3.5a.5.5 0 0 1 .4.8l-5 8.5z"/></svg>
);
