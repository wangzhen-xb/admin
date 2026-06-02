import { withInstall } from '@/utils';
import type { ExtractPropTypes } from 'vue';
import {{ componentName }} from './src/{{ componentName }}.vue';

// Add props import if needed
// import { {{ componentName }}Props } from './src/props';

export const {{ ComponentName }} = withInstall({{ componentName }});
export declare type {{ ComponentName }}Props = Partial<ExtractPropTypes<typeof {{ componentName }}>>;
