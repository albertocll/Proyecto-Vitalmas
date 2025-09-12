import clsx from "clsx";

export default function TextInput({
  id,
  label,
  type = "text",
  error,
  className,
  ...props
}) {
  return (
    <div className="flex flex-col gap-1">
      {label && (
        <label
          htmlFor={id}
          className="text-sm font-medium text-slate-200"
        >
          {label}
        </label>
      )}
      <input
        id={id}
        type={type}
        aria-invalid={!!error}
        aria-describedby={error ? `${id}-error` : undefined}
        className={clsx(
          "rounded-lg border border-slate-600 bg-slate-900 px-3 py-2 text-slate-200 placeholder-slate-500",
          "focus:border-cyan-400 focus:outline-none focus:ring-2 focus:ring-cyan-400",
          error && "border-red-500 focus:border-red-500 focus:ring-red-500",
          className
        )}
        {...props}
      />
      {error && (
        <p
          id={`${id}-error`}
          role="alert"
          className="text-sm text-red-400"
        >
          {error}
        </p>
      )}
    </div>
  );
}
