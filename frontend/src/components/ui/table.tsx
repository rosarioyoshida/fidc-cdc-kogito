import { HTMLAttributes } from "react";
import { cn } from "@/lib/cn";

export function Table(props: HTMLAttributes<HTMLTableElement>) {
  return (
    <div className="overflow-hidden rounded-lg border">
      <div className="overflow-x-auto">
        <table
          {...props}
          className={cn("min-w-full border-collapse bg-surface-raised text-left text-sm", props.className)}
        />
      </div>
    </div>
  );
}
