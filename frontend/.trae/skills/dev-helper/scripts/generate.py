#!/usr/bin/env python3
import os
import sys
import argparse
from pathlib import Path

# Project root directory
PROJECT_ROOT = Path(__file__).parent.parent.parent.parent.parent

# Templates directory
TEMPLATES_DIR = Path(__file__).parent.parent / 'data' / 'templates'

def to_pascal_case(s):
    """Convert string to PascalCase"""
    return ''.join(word.capitalize() for word in s.split(' '))

def to_camel_case(s):
    """Convert string to camelCase"""
    words = s.split(' ')
    return words[0].lower() + ''.join(word.capitalize() for word in words[1:])

def generate_component(name, component_type='basic'):
    """Generate a Vue component"""
    template_path = TEMPLATES_DIR / 'component' / f'{component_type}.vue'
    if not template_path.exists():
        print(f"Error: Template {component_type} not found")
        return
    
    # Read template
    with open(template_path, 'r', encoding='utf-8') as f:
        template = f.read()
    
    # Replace placeholders
    component_name = to_camel_case(name)
    component_name_pascal = to_pascal_case(name)
    template = template.replace('{{ componentName }}', component_name)
    
    # Output path
    output_dir = PROJECT_ROOT / 'src' / 'components' / component_name_pascal
    output_dir.mkdir(parents=True, exist_ok=True)
    src_dir = output_dir / 'src'
    src_dir.mkdir(parents=True, exist_ok=True)
    output_path = src_dir / f'{component_name_pascal}.vue'
    
    # Write output
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(template)
    
    # Generate index.ts
    index_template_path = TEMPLATES_DIR / 'component' / 'index.ts'
    if index_template_path.exists():
        with open(index_template_path, 'r', encoding='utf-8') as f:
            index_template = f.read()
        
        index_template = index_template.replace('{{ componentName }}', component_name)
        index_template = index_template.replace('{{ ComponentName }}', component_name_pascal)
        
        index_path = output_dir / 'index.ts'
        with open(index_path, 'w', encoding='utf-8') as f:
            f.write(index_template)
    
    print(f"Component {component_name_pascal} generated at {output_path}")

def generate_page(name):
    """Generate a Vue page"""
    template_path = TEMPLATES_DIR / 'page' / 'page.vue'
    if not template_path.exists():
        print("Error: Page template not found")
        return
    
    # Read template
    with open(template_path, 'r', encoding='utf-8') as f:
        template = f.read()
    
    # Replace placeholders
    page_name = to_camel_case(name)
    page_name_pascal = to_pascal_case(name)
    template = template.replace('{{ pageName }}', page_name)
    template = template.replace('{{ PageName }}', page_name_pascal)
    
    # Output path
    output_dir = PROJECT_ROOT / 'src' / 'views' / page_name_pascal
    output_dir.mkdir(parents=True, exist_ok=True)
    output_path = output_dir / 'index.vue'
    
    # Write output
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(template)
    
    print(f"Page {page_name_pascal} generated at {output_path}")

def generate_api(name):
    """Generate an API service"""
    template_path = TEMPLATES_DIR / 'api' / 'api.ts'
    if not template_path.exists():
        print("Error: API template not found")
        return
    
    # Read template
    with open(template_path, 'r', encoding='utf-8') as f:
        template = f.read()
    
    # Replace placeholders
    api_name = to_pascal_case(name)
    api_name_lower = to_camel_case(name)
    template = template.replace('{{ apiName }}', api_name)
    template = template.replace('{{ apiNameLower }}', api_name_lower)
    
    # Output path
    output_dir = PROJECT_ROOT / 'src' / 'api' / api_name_lower
    output_dir.mkdir(parents=True, exist_ok=True)
    output_path = output_dir / f'{api_name_lower}.api.ts'
    
    # Write output
    with open(output_path, 'w', encoding='utf-8') as f:
        f.write(template)
    
    print(f"API service {api_name} generated at {output_path}")

def main():
    parser = argparse.ArgumentParser(description='Generate code for Vue 3 + TypeScript project')
    subparsers = parser.add_subparsers(dest='command', help='Command to execute')
    
    # Component command
    component_parser = subparsers.add_parser('component', help='Generate a Vue component')
    component_parser.add_argument('name', help='Component name')
    component_parser.add_argument('--type', default='basic', help='Component type (default: basic)')
    
    # Page command
    page_parser = subparsers.add_parser('page', help='Generate a Vue page')
    page_parser.add_argument('name', help='Page name')
    
    # API command
    api_parser = subparsers.add_parser('api', help='Generate an API service')
    api_parser.add_argument('name', help='API name')
    
    args = parser.parse_args()
    
    if args.command == 'component':
        generate_component(args.name, args.type)
    elif args.command == 'page':
        generate_page(args.name)
    elif args.command == 'api':
        generate_api(args.name)
    else:
        parser.print_help()

if __name__ == '__main__':
    main()
